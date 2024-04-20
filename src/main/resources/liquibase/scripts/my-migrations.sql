-- liquibase formatted sql

-- changeset michael:2
CREATE TABLE IF NOT EXISTS public.notification_task
(
    id bigint PRIMARY KEY,
    chat_id bigint,
    note character varying(255),
    date_time character varying(255)
)