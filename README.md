# Ponir Kumer Saha - Portfolio & Blog CMS

A responsive personal website built with Spring Boot, Thymeleaf, PostgreSQL, Flyway, and Spring Security. The visual direction takes inspiration from editorial engineering portfolios while using an original layout, content system, and design.

## What is included

- Public home, about, experience, projects, project detail, blog, article, and contact pages
- Résumé-based starter content and the supplied professional portrait
- Dynamic profile, metrics, social links (including WhatsApp), skills, work history, projects, and blog posts
- Blog search, drafts, publication status, featured posts, slugs, and optional article images
- Secure admin CMS at `/admin`
- Secure local image uploads for the profile, projects, and blog posts
- Contact inbox stored in PostgreSQL, including unread state and email-reply links
- Dark/light themes and a responsive mobile navigation
- Grouped capability areas, a focused experience timeline, and linked project case studies
- Structured technical articles with headings, lists, inline code, and fenced code blocks
- PostgreSQL schema management with Flyway
- Docker Compose database setup
- Disposable H2 preview profile for UI work when Docker is unavailable
- Integration tests for public rendering, security, contact persistence, CMS creation, and Flyway schema validation

## Requirements

- Java 21+
- Maven 3.9+
- Docker Desktop or another PostgreSQL 17 instance

## Run with PostgreSQL

### Local lifecycle script

The helper script manages PostgreSQL, rebuilds when source files change, starts the application in the background, and stores its PID and log under `.run/`:

```bash
./run-local.sh start local
./run-local.sh status local
./run-local.sh restart local
./run-local.sh stop local
./run-local.sh logs local
```

Docker Desktop must be running. Set `ADMIN_PASSWORD` in your shell or a local `.env` file before starting.

### Manual start

1. Start Docker Desktop.
2. Start the database:

   ```bash
   docker compose up -d
   ```

3. Set a private admin password. The checked-in value is only a development fallback:

   ```bash
   export ADMIN_USERNAME=admin
   export ADMIN_PASSWORD='replace-with-a-long-random-password'
   ```

4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

5. Open:

   - Portfolio: <http://localhost:9090>
   - Admin CMS: <http://localhost:9090/admin>

PostgreSQL data is retained in the `portfolio-postgres` Docker volume. Flyway creates the schema automatically. Starter content is inserted only when each content table is empty.

## Fast local preview

This mode uses an in-memory H2 database and resets whenever the application stops:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=preview
```

The default preview login is `admin` / `ChangeMe123!`. Never use that password on a deployed site.

## Configuration

| Environment variable | Default | Purpose |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5434/portfolio` | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `portfolio` | Database user |
| `SPRING_DATASOURCE_PASSWORD` | `portfolio` | Database password |
| `POSTGRES_PORT` | `5434` | Host port used by the Compose PostgreSQL container |
| `APP_PORT` | `9090` | Local application port used by `run-local.sh` |
| `SERVER_PORT` | `9090` | Spring Boot server port; overrides `APP_PORT` in the launcher |
| `ADMIN_USERNAME` | `admin` | CMS username |
| `ADMIN_PASSWORD` | `ChangeMe123!` | CMS password; change before deployment |
| `PORTFOLIO_UPLOAD_DIR` | `uploads` | Persistent directory for admin-uploaded images |

The Compose database values can also be set with `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, and `POSTGRES_PORT`. See `.env.example`.

## Content management

After signing in, the CMS provides:

- **Profile:** identity, hero copy, biography, contact details, configurable LinkedIn/GitHub/WhatsApp links, portrait upload, résumé path, and metrics
- **Skills:** category, proficiency, display order, and homepage feature flag
- **Experience:** company, title, location, dates, summary, project highlights, skills, achievements, current-role flag, and display order
- **Projects:** title, slug, description, tech stack, project image, links, visibility, homepage feature flag, and display order
- **Blog posts:** title, slug, category, excerpt, content, featured image, draft/published status, and featured flag
- **Messages:** contact submissions, read state, reply link, and deletion

Article bodies support a focused Markdown subset: `##`/`###` headings, `-` bullet lists, inline backtick code, and fenced code blocks such as <code>```java</code>. Input is HTML-escaped before rendering. Project descriptions remain escaped plain text with paragraph breaks preserved.

Uploaded images are validated as JPG, PNG, WebP, GIF, or AVIF files up to 10 MB and served from `/uploads/**`. Keep `PORTFOLIO_UPLOAD_DIR` on persistent storage in production; the local `uploads/` directory is ignored by Git.

## Application architecture

The web layer follows a controller → service → repository boundary. Controllers handle routing, validation results, and view models; transactional services own content workflows and persistence access. Lombok `@RequiredArgsConstructor` keeps constructor injection concise without using JPA-unfriendly entity `@Data` generation.

## Tests and build

```bash
mvn test
mvn package
```

The test profile runs Flyway against an H2 PostgreSQL-compatible database and asks Hibernate to validate the resulting schema before exercising the web flows.

## Production notes

- Set unique database and admin passwords through secrets/environment variables.
- Put the app behind HTTPS using a reverse proxy or managed platform.
- Back up the PostgreSQL volume/database.
- For multiple administrators or password recovery, replace the environment-backed admin account with database-backed users.
- Contact submissions are stored in the CMS; they do not send email automatically.
