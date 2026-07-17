# xG Match Intel

Football analytics API with an AI agent layer. A Spring Boot service ingests open
football data, computes analytical metrics (xG aggregates, team form, head-to-head
records), and exposes them via a clean REST API. On top of it, an LLM-powered agent
generates match previews / post-match reports and answers free-form analytical
questions by calling the same API as its tools.

> **Status:** early skeleton. The project structure, infrastructure, and the agent
> `LlmClient` seam are in place; ingestion, the metrics engine, and the agent loop
> are being built out. See [`docs/`](docs/) for the full plan.

## Tech stack

Java 21 · Spring Boot 3.3 · PostgreSQL 16 (Flyway) · Redis 7 · springdoc-openapi ·
Anthropic API (Claude) for the agent · Docker Compose · GitHub Actions. Full
breakdown in [`docs/tech-stack.md`](docs/tech-stack.md).

## Quickstart

Prerequisites: JDK 21 and Maven 3.9+ (or just Docker).

### With Docker Compose (app + Postgres + Redis)

```bash
cp .env.example .env   # optionally set ANTHROPIC_API_KEY / LLM_PROVIDER=anthropic
docker compose up --build
```

Then open the API docs at <http://localhost:8080/swagger-ui.html>.

### Locally (needs Postgres + Redis running)

```bash
mvn spring-boot:run
```

The agent runs against a deterministic **stub** LLM by default (`LLM_PROVIDER=stub`),
so the demo path works offline with no API key. Set `LLM_PROVIDER=anthropic` and
`ANTHROPIC_API_KEY` to use the real Claude API.

### Try it

```bash
curl http://localhost:8080/api/v1/teams
curl -X POST http://localhost:8080/api/v1/agent/ask \
  -H 'Content-Type: application/json' \
  -d '{"question": "How does Arsenal play away against top-6?"}'
```

## Build & test

```bash
mvn verify
```

CI (build + tests) runs on every push and pull request via GitHub Actions.

## How it was built

This project is developed with [Claude Code](https://claude.com/claude-code) as an
AI pair-programmer — planning docs, scaffolding, and incremental features. The
architecture decisions live in [`docs/`](docs/) and [`docs/adr/`](docs/adr/).

## Data attribution

Football event data (including per-shot xG) is from **StatsBomb Open Data**, free
for non-commercial use with attribution. Fixtures/standings optionally from
**football-data.org**. This project is non-commercial and for portfolio purposes.

## License

TBD.
