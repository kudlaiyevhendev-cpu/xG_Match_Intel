# xG Match Intel — Tech Stack

Complete list of technologies, grouped by area. Versions are targets at project start; pin exact versions in `pom.xml`.

## Language & Runtime

| Technology | Version | Used for |
|---|---|---|
| Java | 21 (LTS) | Entire codebase; records, pattern matching, virtual threads where useful |
| Spring Boot | 3.x | Application framework: Web MVC, Validation, Scheduling, Cache abstraction, Actuator |

## Data Sources

| Technology | Used for |
|---|---|
| StatsBomb Open Data | Primary source: event-level data incl. per-shot xG (free, non-commercial, attribution required) |
| football-data.org API | Secondary: fixtures, standings (free tier, 10 req/min) |
| Spring `RestClient` | HTTP clients for both sources |

## Persistence & Caching

| Technology | Used for |
|---|---|
| PostgreSQL 16 | Normalized domain (teams, matches, stats, shots), ingestion job queue, agent traces; JSONB for raw payload archive |
| Spring Data JPA (Hibernate) | Repositories, entity mapping |
| Flyway | Versioned schema migrations, run on startup |
| Redis 7 | Cache: computed metrics, LLM responses/reports (TTL + data-version invalidation) |
| Spring Cache | Cache abstraction over Redis for the metrics engine |

## Ingestion Pipeline

| Technology | Used for |
|---|---|
| Spring `@Scheduled` | Incremental sync triggers per competition/season |
| Postgres-backed job queue (`SELECT ... FOR UPDATE SKIP LOCKED`) | Backfill jobs: idempotent, restart-safe, no extra broker (ADR: deliberately no Kafka) |
| Token-bucket rate limiter (in-app) | Respecting football-data.org free-tier limits |

## Agent Layer (LLM)

| Technology | Used for |
|---|---|
| Anthropic API (Claude) | Tool-use loop: free-form Q&A; preview & post-match report generation |
| Anthropic Java SDK (or plain REST via `RestClient`) | API integration |
| `LlmClient` abstraction (in-repo) | `AnthropicLlmClient` for real calls; `StubLlmClient` for tests and offline demo |
| Externalized prompt templates (`resources/prompts/`) | Versioned system prompts and report templates |

## API & Documentation

| Technology | Used for |
|---|---|
| Spring Web MVC | REST controllers `/api/v1/...` |
| springdoc-openapi | OpenAPI spec + Swagger UI; single source of truth for agent tool definitions |
| RFC 9457 Problem Details (`@ControllerAdvice`) | Uniform error responses |

## Testing & Quality

| Technology | Used for |
|---|---|
| JUnit 5 + Mockito | Unit tests: metrics engine, prompt builders, tool schema mapping |
| Testcontainers (Postgres, Redis) | Repository and full-stack integration tests |
| WireMock | Stubbing StatsBomb / football-data.org in ingestion tests |
| MockMvc | API slice tests |
| JaCoCo | Coverage reports (CI artifact) |
| Spotless + Checkstyle (optionally ErrorProne) | Formatting and static analysis |

## Tooling & Infrastructure

| Technology | Used for |
|---|---|
| Maven | Build, dependency management |
| Docker + Docker Compose | Local stack: app + postgres + redis, one-command startup |
| Multi-stage Dockerfile (JDK build → JRE runtime) | Slim runtime image |
| GitHub Actions | CI: build, test, static analysis, Docker image |
| Spring Actuator | Health checks (db, redis), ingestion metrics |

## Key External Dependencies Summary

- **Paid/API**: Anthropic API (LLM calls) — the only paid dependency; mitigated by Redis response caching, token budgets, and the stub client for tests/demo.
- **Rate-limited free**: football-data.org (10 req/min free tier).
- **Attribution required**: StatsBomb Open Data (non-commercial use, attribution in README).
- **Everything else** is open source and runs in Docker Compose.
