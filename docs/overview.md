# xG Match Intel — Overview

Football analytics API with an AI agent layer. A Spring Boot service ingests open football data, computes analytical metrics (xG aggregates, team form, head-to-head records), and exposes them via a clean REST API. On top of it, an LLM-powered agent generates match previews and post-match reports, and answers free-form analytical questions ("How does Arsenal perform away against top-6 sides?") by calling the same API as tools.

## Product Goals

1. **Portfolio flagship.** Demonstrate senior-level backend engineering: layered architecture, data ingestion pipeline, caching, persistence, testing, and API design — all in one coherent, runnable project.
2. **Showcase agentic tool-use.** The agent layer is not a chatbot bolted on the side; it consumes the project's own REST API as its tool set, demonstrating practical LLM function-calling / tool-use skills.
3. **Document the AI-assisted workflow.** The README describes how the project was built with Claude Code — a differentiating signal for hiring teams evaluating AI-fluent engineers.

## Scope

### In scope (MVP)

- **Data ingestion** from StatsBomb Open Data (primary; rich event-level data incl. shot xG) with football-data.org as an optional secondary source for fixtures/standings.
- **Metrics engine**: per-match and rolling xG / xGA, team form (last N matches), home/away splits, head-to-head summaries, league table context.
- **REST API** (versioned, `/api/v1/...`) exposing teams, matches, metrics, and comparisons. Documented with OpenAPI/Swagger UI.
- **Agent layer**: LLM with tool definitions mapped to the REST API. Two modes:
  - *Generated reports*: match preview and post-match summary endpoints.
  - *Free-form Q&A*: natural-language question → tool calls → grounded answer.
- **Infrastructure**: PostgreSQL for persistence, Redis for caching computed metrics and LLM responses, scheduled ingestion jobs with a lightweight queue for backfill tasks.
- **Quality**: unit + integration tests (Testcontainers), CI pipeline, Docker Compose for one-command local startup.

### Out of scope (explicitly)

- Live/in-play data and websockets (open data sources are historical/delayed).
- Training custom xG models — StatsBomb event data already carries shot xG; we aggregate, not model. (A custom logistic-regression xG model is listed as a stretch goal.)
- Frontend beyond Swagger UI and (optionally) a minimal demo page.
- User accounts, auth beyond a simple API key, billing.

## Constraints

- **Data licensing.** StatsBomb Open Data is free for non-commercial use with attribution — the README must carry the required attribution. football-data.org free tier: 10 req/min, limited competitions; the ingestion scheduler must respect rate limits.
- **Cost.** LLM calls are the only paid dependency. Mitigations: response caching in Redis, small default model, strict token budgets per request, and a "dry-run" mode with a stubbed LLM for tests and demos.
- **Runtime.** Must run locally on a laptop via Docker Compose (Postgres + Redis + app). No cloud dependency required for the demo path.
- **Language/stack.** Java 21, Spring Boot 3.x. No Kotlin/Groovy in main code.
- **Solo project.** Architecture should be impressive but maintainable by one person; avoid microservice sprawl — a well-structured modular monolith.

## Success Criteria

1. `docker compose up` → seeded database → working Swagger UI within minutes, no manual steps.
2. API answers core analytical queries: team form, xG trends, head-to-head, home/away splits — each covered by integration tests.
3. Agent answers at least the canonical demo question ("How does Arsenal play away against top-6?") with a grounded, tool-call-backed response, and the tool-call trace is visible/loggable.
4. Preview and post-match report generation works for any match in the ingested dataset.
5. Test coverage: meaningful unit tests for the metrics engine + Testcontainers-based integration tests for repositories and API; CI green on every push.
6. README tells the story: what it does, how to run it, architecture diagram, how it was built with Claude Code, data attribution.
7. A reviewer with 10 minutes can clone, run, and see the agent answer a question — the "wow path" is short.
