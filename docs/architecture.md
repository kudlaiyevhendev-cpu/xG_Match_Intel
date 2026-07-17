# xG Match Intel — Architecture

## Stack

| Concern | Choice | Notes |
|---|---|---|
| Language / runtime | Java 21 (LTS) | Records, pattern matching, virtual threads where useful |
| Framework | Spring Boot 3.x | Web MVC, Validation, Scheduling, Cache abstraction |
| Persistence | PostgreSQL 16 | Spring Data JPA + Flyway migrations |
| Cache | Redis 7 | Spring Cache (metrics), plus explicit cache for LLM responses |
| Ingestion queue | Postgres-backed job table (or Spring `@Async` + `TaskExecutor`) | Deliberately no Kafka/Rabbit — right-sized for the load; documented trade-off |
| HTTP clients | Spring `RestClient` | StatsBomb GitHub raw JSON; football-data.org REST |
| LLM | Anthropic API (Claude) via Java SDK / plain REST | Tool-use (function calling); stub implementation for tests |
| API docs | springdoc-openapi | Swagger UI at `/swagger-ui.html` |
| Testing | JUnit 5, Mockito, Testcontainers (Postgres, Redis), WireMock | WireMock stubs external data sources |
| Build / CI | Maven, GitHub Actions | Build, test, Docker image |
| Local runtime | Docker Compose | app + postgres + redis |

## Module / Layer Layout

Modular monolith with strict layering. Packages by feature, layers inside each feature:

```
com.xgintel
├── ingestion/        # fetch + normalize external data
├── catalog/          # teams, competitions, matches (domain + API)
├── metrics/          # xG aggregates, form, H2H, splits
├── agent/            # LLM orchestration, tool registry, prompts
├── api/              # REST controllers, DTOs, error handling
├── config/           # Spring configuration, cache, scheduling
└── common/           # shared value objects, exceptions
```

Dependency rule: `api → metrics/catalog/agent → ingestion` never reversed; `common` visible to all. Controllers never touch repositories directly; services own transactions.

## Runtime Responsibilities

### 1. Ingestion pipeline

- **Scheduler** (`@Scheduled`) triggers incremental sync per competition/season; a manual admin endpoint triggers backfill.
- **Job queue**: backfill work is split into per-match jobs persisted in a `ingestion_job` table (`PENDING / RUNNING / DONE / FAILED`, attempts, last_error). A worker polls with `SELECT ... FOR UPDATE SKIP LOCKED` — idempotent, restart-safe, and demonstrates queue semantics without extra infrastructure.
- **Normalization**: raw StatsBomb events → domain entities (Match, TeamMatchStats with shots, xG, results). Raw payloads optionally archived in a JSONB column for reprocessing.
- **Rate limiting**: token-bucket guard around football-data.org client (10 req/min free tier).

### 2. Metrics engine

- Pure, deterministic services computing: rolling xG/xGA over last N matches, points-per-game form, home/away splits, head-to-head aggregates, opponent-tier filters (e.g., "vs top-6 by table position").
- Results cached in Redis with TTL keyed by `(metric, teamId, params, dataVersion)`; `dataVersion` bumps on ingestion completion so caches invalidate naturally.
- Heavy aggregates computed in SQL where sensible (window functions), thin Java on top.

### 3. REST API

- `/api/v1/teams`, `/api/v1/matches`, `/api/v1/metrics/...`, `/api/v1/compare` — resource-oriented, paginated, validated.
- Problem-details (`RFC 9457`) error responses via `@ControllerAdvice`.
- OpenAPI spec is the contract the agent's tools are generated/derived from — one source of truth.

### 4. Agent layer

- **Tool registry**: each exposed analytical capability is described as a tool (name, JSON schema, handler). Handlers call the same application services the REST layer uses (in-process, not HTTP loopback — faster and transactional; the mapping to public endpoints is documented).
- **Loop**: user question → system prompt + tool defs → model returns tool calls → execute → feed results back → final grounded answer. Max-iterations and token budget enforced.
- **Report generation**: preview = fixture context + both teams' form/H2H metrics → templated prompt; post-match = match stats + xG story → summary. Responses cached in Redis by match id + report type.
- **Observability**: every agent run logs the tool-call trace (question, tools invoked, args, durations) — this trace is a demo feature, not just debugging.
- **LLM abstraction**: `LlmClient` interface with `AnthropicLlmClient` and `StubLlmClient` (tests, offline demo).

## Data Flow

```
StatsBomb / football-data.org
        │  (scheduled fetch / backfill jobs)
        ▼
  Ingestion service ──► Postgres (normalized domain + raw JSONB)
        │                    │
        │ bump dataVersion   │ SQL aggregates
        ▼                    ▼
      Redis ◄────────── Metrics engine ◄──────────┐
        ▲                    ▲                    │ in-process tool calls
        │ cached reports     │                    │
        └─────────────── Agent layer ─────────────┘
                             ▲
                             │ REST /api/v1/agent/ask, /reports
                        API layer ◄── clients / Swagger UI
```

## Persistence

- **Flyway** migrations, versioned, run on startup.
- Core tables: `competition`, `season`, `team`, `match`, `team_match_stats`, `shot` (optional, for per-shot xG detail), `ingestion_job`, `agent_run` (trace), `report_cache` (if not Redis-only).
- Indexing strategy documented per query pattern (team+date for form, pair index for H2H).
- Read models kept simple; no CQRS ceremony — noted as a conscious trade-off in the README.

## Verification

- **Unit tests**: metrics engine with hand-crafted fixtures (known xG inputs → asserted aggregates); prompt builders; tool schema mapping.
- **Integration tests**: Testcontainers Postgres + Redis; repository queries; full API slice tests with MockMvc; ingestion against WireMock-served StatsBomb fixtures.
- **Agent tests**: `StubLlmClient` scripted to emit tool calls → assert the loop executes tools and grounds the answer; one optional live smoke test behind an env flag.
- **CI**: GitHub Actions — build, test, static analysis (Spotless/Checkstyle, optionally ErrorProne), Docker image build.
- **Runtime checks**: Spring Actuator health (db, redis), ingestion job metrics exposed via `/actuator`.
