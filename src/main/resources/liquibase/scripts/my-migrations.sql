-- liquibase formatted sql

-- changeset michael:3
CREATE TABLE IF NOT EXISTS public.notification_task
(
    id bigint PRIMARY KEY,
    chat_id bigint,
    note CHARACTER VARYING(255),
    date_time DATETIME
)