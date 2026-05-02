# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Talk Tracker is a Spring Boot 4 + Vaadin 25 (Flow) web app for browsing JAX 2026 conference talks. It features a conversational AI assistant (Spring AI + OpenAI) that filters the talk grid via tool-calling. The H2 in-memory database is seeded with the real JAX 2026 schedule on startup.

## Build & Run Commands

```bash
# Run the application (requires OPENAI_API_KEY env var for AI features)
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=TalkDetailViewTest
```

## Key Versions

- Java 25, Spring Boot 4.0.6, Vaadin 25.1.4, Spring AI 2.0.0-M5

## Architecture

**Single-view app** — `TalkListView` is the root route (`@Route("")`), with a filterable `Grid<Talk>` on the left and a chat UI (`MessageList` + `MessageInput`) on the right.

### Layers

- **`data/`** — JPA entity `Talk` (with nested enums `Track` and `Format`), `TalkRepository` (extends `JpaSpecificationExecutor` + `@EntityGraph` for eager track loading), `DataInitializer` seeds the real JAX 2026 schedule (5 days, May 4–8 2026).
- **`ui/talks/`** — `TalkListView` contains the Grid, chat components, and all Spring AI tool-calling logic directly.

### Key Patterns

- **Server Push** — `@Push` on `TalkTrackerApplication` enables UI updates from background threads. AI streaming tokens are pushed via `ui.access()`.
- **Aura theme** — Uses `@StyleSheet(Aura.STYLESHEET)` (not Lumo).
- **AI tool-calling** — `TalkListView` exposes `@Tool`-annotated methods (`getAllTalks()`, `filterTalks(List<String> ids)`, `currentLocalDateTime()`) to the `ChatClient`. The AI calls `filterTalks` to update the grid based on natural-language queries.
- **Streaming chat** — `ChatClient` response streams token-by-token into the `MessageList` via `ui.access()`.
- **Eager track loading** — `TalkRepository` uses `@EntityGraph(attributePaths = "tracks")` on `findAll()` overrides to avoid N+1 queries for the `TALK_TRACKS` join table.

### Data Model (`Talk` entity)

Fields: `id`, `title`, `speaker`, `tracks` (`List<Track>`, ElementCollection), `format` (`Format` enum), `startDate`, `startTime`, `endTime`, `room`.

Enums — `Track`: AGILE, AGILE_FLOW, ARCH, CLOUD, CORE_JAVA, DATA_ML, DEVOPS, GEN_AI, MICRO, PERF_SEC, SERVER_JAVA, WEB_JS.  
Enums — `Format`: KEYNOTE, SESSION, WORKSHOP, PANEL, LAB, SHORTTALK.

## Testing

Tests use Vaadin's **browserless testing** (`SpringBrowserlessTest` from `browserless-test-junit6`). Tests extend `SpringBrowserlessTest` with `@SpringBootTest` and navigate to views via `navigate(ViewClass.class)`. View fields used in tests are package-private (not private).
