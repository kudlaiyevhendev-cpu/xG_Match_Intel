# ADR 0001: Postgres-backed ingestion job queue (no Kafka/RabbitMQ)

- **Status:** Accepted
- **Date:** 2025-07-17

## Context

The ingestion pipeline needs to run backfill work as discrete, restart-safe,
idempotent units (one job per match). This is classic queue territory, and the
obvious reach is for a dedicated broker (Kafka, RabbitMQ, SQS).

## Decision

Use a Postgres-backed job table (`ingestion_job`) polled with
`SELECT ... FOR UPDATE SKIP LOCKED`, rather than introducing a message broker.

Job rows carry a status (`PENDING / RUNNING / DONE / FAILED`), attempt count, and
last error. A worker claims rows with `SKIP LOCKED` so multiple workers never grab
the same job, and unfinished work is naturally reclaimed after a restart.

## Consequences

**Positive**
- No extra infrastructure — the demo runs on `docker compose up` with just
  Postgres + Redis + app. One less moving part to operate for a solo project.
- Transactional: claiming a job and writing its result happen in the same database
  the domain data lives in, so there is no dual-write / outbox problem.
- `FOR UPDATE SKIP LOCKED` demonstrates real queue semantics (at-least-once,
  competing consumers) without broker ceremony.

**Negative / trade-offs**
- Not built for high-throughput fan-out; this is right-sized for periodic ingestion
  of open football data, not a firehose.
- Polling has a small latency floor vs. push-based delivery — acceptable here.

If throughput ever outgrows this, the worker interface is narrow enough to swap in
a real broker behind it.
