-- Initial catalog schema: competitions and teams.
-- Metrics, matches, shots, ingestion_job, and agent_run tables arrive in later migrations.

CREATE TABLE competition (
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    country VARCHAR(255)
);

CREATE TABLE team (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    short_name VARCHAR(64)
);
