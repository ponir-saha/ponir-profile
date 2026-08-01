create table site_profile (
    id bigint primary key,
    full_name varchar(160) not null,
    headline varchar(300) not null,
    hero_eyebrow varchar(160) not null,
    hero_title varchar(320) not null,
    introduction text not null,
    about text not null,
    email varchar(180) not null,
    phone varchar(80),
    location varchar(180),
    linkedin_url varchar(500),
    github_url varchar(500),
    portrait_url varchar(500),
    resume_url varchar(500),
    availability varchar(240),
    years_experience integer not null default 0,
    projects_delivered integer not null default 0,
    engineers_mentored integer not null default 0,
    updated_at timestamp with time zone not null
);

create table skills (
    id bigserial primary key,
    name varchar(160) not null,
    category varchar(120) not null,
    proficiency integer not null,
    sort_order integer not null default 0,
    featured boolean not null default false
);

create table experiences (
    id bigserial primary key,
    company varchar(200) not null,
    title varchar(200) not null,
    location varchar(180),
    start_label varchar(80) not null,
    end_label varchar(80),
    current_position boolean not null default false,
    summary text not null,
    sort_order integer not null default 0
);

create table projects (
    id bigserial primary key,
    title varchar(220) not null,
    slug varchar(240) not null unique,
    eyebrow varchar(100),
    summary varchar(600) not null,
    description text not null,
    tech_stack varchar(800),
    github_url varchar(500),
    live_url varchar(500),
    featured boolean not null default false,
    published boolean not null default true,
    sort_order integer not null default 0,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

create table blog_posts (
    id bigserial primary key,
    title varchar(260) not null,
    slug varchar(280) not null unique,
    excerpt varchar(700) not null,
    content text not null,
    category varchar(120) not null,
    featured_image_url varchar(500),
    status varchar(30) not null,
    featured boolean not null default false,
    published_at timestamp with time zone,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

create index idx_blog_posts_status_published_at on blog_posts(status, published_at desc);

create table contact_messages (
    id bigserial primary key,
    name varchar(160) not null,
    email varchar(180) not null,
    subject varchar(240) not null,
    message text not null,
    is_read boolean not null default false,
    created_at timestamp with time zone not null
);
