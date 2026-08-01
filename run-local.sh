#!/usr/bin/env bash

set -Eeuo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMMAND="${1:-help}"
ENVIRONMENT="${2:-local}"
RUN_DIR="$ROOT_DIR/.run"
PID_FILE="$RUN_DIR/profile-bio.pid"
LOG_FILE="$RUN_DIR/profile-bio.log"
JAR_FILE="$ROOT_DIR/target/profile-bio-0.0.1-SNAPSHOT.jar"

if [[ "$ENVIRONMENT" != "local" ]]; then
    echo "Unsupported environment: $ENVIRONMENT (expected: local)" >&2
    exit 2
fi

if [[ -f "$ROOT_DIR/.env" ]]; then
    set -a
    # shellcheck disable=SC1091
    source "$ROOT_DIR/.env"
    set +a
fi

PORT="${SERVER_PORT:-${APP_PORT:-9090}}"
DB_NAME="${POSTGRES_DB:-portfolio}"
DB_USER="${POSTGRES_USER:-portfolio}"
DB_PASSWORD="${POSTGRES_PASSWORD:-portfolio}"
DB_PORT="${POSTGRES_PORT:-5434}"
SPRING_DB_URL="${SPRING_DATASOURCE_URL:-jdbc:postgresql://localhost:$DB_PORT/$DB_NAME}"
SPRING_DB_USER="${SPRING_DATASOURCE_USERNAME:-$DB_USER}"
SPRING_DB_PASSWORD="${SPRING_DATASOURCE_PASSWORD:-$DB_PASSWORD}"

mkdir -p "$RUN_DIR"

pid_value() {
    [[ -f "$PID_FILE" ]] && tr -d '[:space:]' < "$PID_FILE"
}

is_running() {
    local pid
    pid="$(pid_value)"
    [[ "$pid" =~ ^[0-9]+$ ]] || return 1
    kill -0 "$pid" 2>/dev/null
}

is_our_process() {
    local pid
    pid="$(pid_value)"
    [[ "$pid" =~ ^[0-9]+$ ]] || return 1
    ps -p "$pid" -o command= 2>/dev/null | grep -Fq "profile-bio-0.0.1-SNAPSHOT.jar"
}

port_is_in_use() {
    if command -v lsof >/dev/null 2>&1; then
        lsof -nP -iTCP:"$PORT" -sTCP:LISTEN >/dev/null 2>&1
    elif command -v nc >/dev/null 2>&1; then
        nc -z 127.0.0.1 "$PORT" >/dev/null 2>&1
    else
        return 1
    fi
}

require_docker() {
    if ! command -v docker >/dev/null 2>&1; then
        echo "Docker is not installed or is not on PATH." >&2
        exit 1
    fi
    if ! docker info >/dev/null 2>&1; then
        echo "Docker is not running. Start Docker Desktop and try again." >&2
        exit 1
    fi
    if ! docker compose version >/dev/null 2>&1; then
        echo "Docker Compose is unavailable." >&2
        exit 1
    fi
}

start_postgres() {
    require_docker
    echo "Starting PostgreSQL..."
    docker compose -f "$ROOT_DIR/compose.yaml" up -d postgres

    local attempt
    for attempt in {1..40}; do
        if docker compose -f "$ROOT_DIR/compose.yaml" exec -T postgres \
            pg_isready -U "$DB_USER" -d "$DB_NAME" >/dev/null 2>&1; then
            echo "PostgreSQL is ready."
            return 0
        fi
        sleep 1
    done

    echo "PostgreSQL did not become ready within 40 seconds." >&2
    docker compose -f "$ROOT_DIR/compose.yaml" logs --tail=40 postgres >&2 || true
    exit 1
}

build_if_needed() {
    if [[ ! -f "$JAR_FILE" ]] || find "$ROOT_DIR/pom.xml" "$ROOT_DIR/src" -type f -newer "$JAR_FILE" -print -quit | grep -q .; then
        echo "Building application..."
        (cd "$ROOT_DIR" && mvn -q -DskipTests package)
    fi
}

wait_for_application() {
    local attempt
    for attempt in {1..60}; do
        if ! is_running; then
            echo "Application stopped during startup. Recent log output:" >&2
            tail -n 50 "$LOG_FILE" >&2 || true
            return 1
        fi

        if command -v curl >/dev/null 2>&1; then
            if curl -fsS "http://127.0.0.1:$PORT/" >/dev/null 2>&1; then
                return 0
            fi
        elif command -v nc >/dev/null 2>&1 && nc -z 127.0.0.1 "$PORT" >/dev/null 2>&1; then
            return 0
        elif (echo > "/dev/tcp/127.0.0.1/$PORT") >/dev/null 2>&1; then
            return 0
        fi
        sleep 1
    done

    echo "Application did not become ready within 60 seconds." >&2
    tail -n 50 "$LOG_FILE" >&2 || true
    return 1
}

start_app() {
    if is_running; then
        if is_our_process; then
            echo "Portfolio is already running (PID $(pid_value)) at http://localhost:$PORT"
            return 0
        fi
        echo "Removing a stale PID file that points to another process."
        rm -f "$PID_FILE"
    fi

    if port_is_in_use; then
        echo "Port $PORT is already in use. Set APP_PORT to a free port in .env and try again." >&2
        exit 1
    fi

    start_postgres
    build_if_needed
    : > "$LOG_FILE"

    echo "Starting portfolio..."
    (
        cd "$ROOT_DIR"
        SPRING_DATASOURCE_URL="$SPRING_DB_URL" \
        SPRING_DATASOURCE_USERNAME="$SPRING_DB_USER" \
        SPRING_DATASOURCE_PASSWORD="$SPRING_DB_PASSWORD" \
        nohup java -jar "$JAR_FILE" --server.port="$PORT" >> "$LOG_FILE" 2>&1 &
        echo $! > "$PID_FILE"
    )

    if wait_for_application; then
        echo "Portfolio started successfully."
        echo "Website: http://localhost:$PORT"
        echo "Admin:   http://localhost:$PORT/admin"
        echo "Log:     $LOG_FILE"
    else
        exit 1
    fi
}

stop_app() {
    if ! is_running; then
        rm -f "$PID_FILE"
        echo "Portfolio is not running."
        return 0
    fi

    local pid attempt
    pid="$(pid_value)"
    if ! is_our_process; then
        echo "Refusing to stop PID $pid because it is not this portfolio process." >&2
        exit 1
    fi
    echo "Stopping portfolio (PID $pid)..."
    kill "$pid"

    for attempt in {1..30}; do
        if ! kill -0 "$pid" 2>/dev/null; then
            rm -f "$PID_FILE"
            echo "Portfolio stopped."
            return 0
        fi
        sleep 0.5
    done

    echo "Portfolio did not stop gracefully; forcing the same verified process to exit."
    kill -9 "$pid"
    rm -f "$PID_FILE"
}

show_status() {
    local app_status=1
    if is_running; then
        echo "Application: RUNNING (PID $(pid_value))"
        echo "Website:     http://localhost:$PORT"
        echo "Admin:       http://localhost:$PORT/admin"
        echo "Log:         $LOG_FILE"
        app_status=0
    else
        rm -f "$PID_FILE"
        echo "Application: STOPPED"
    fi

    if command -v docker >/dev/null 2>&1 && docker info >/dev/null 2>&1; then
        echo
        docker compose -f "$ROOT_DIR/compose.yaml" ps postgres
    else
        echo "PostgreSQL:  UNKNOWN (Docker is not running)"
    fi
    return "$app_status"
}

show_help() {
    cat <<'HELP'
Usage: ./run-local.sh <command> local

Commands:
  start    Start PostgreSQL, build if needed, and start the application
  restart  Restart the application and ensure PostgreSQL is ready
  status   Show application and PostgreSQL status
  stop     Stop the application (PostgreSQL remains running)
  logs     Follow the application log
HELP
}

case "$COMMAND" in
    start) start_app ;;
    restart) stop_app; start_app ;;
    status) show_status ;;
    stop) stop_app ;;
    logs) touch "$LOG_FILE"; tail -f "$LOG_FILE" ;;
    help|-h|--help) show_help ;;
    *) echo "Unknown command: $COMMAND" >&2; show_help; exit 2 ;;
esac
