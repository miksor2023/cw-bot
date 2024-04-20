-- liquibase formatted sql

-- changeset michael:1
CREATE TABLE IF NOT EXISTS public.notification_task
(
    id bigint PRIMARY KEY,
    cat_id bigint PRIMARY KEY,
    brand character varying(255),
    model character varying(255),
    price integer
)