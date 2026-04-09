# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Talk Tracker is a Spring Boot 4 + Vaadin 25 (Flow) web app for managing conference talks with AI-powered summarization via Spring AI (OpenAI). It uses an in-memory H2 database seeded with sample data on startup.

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

- Java 25, Spring Boot 4.0.5, Vaadin 25.1.1, Spring AI 2.0.0-M4

## Architecture

**Single-view app** — `TalkDetailsView` is the root route (`@Route("")`), a master-detail layout with a filterable Grid and a read-only description form with AI summarize button.

### Layers

- **`data/`** — JPA entities (`Talk`, `Category`), Spring Data repositories. `TalkRepository` extends `JpaSpecificationExecutor` for dynamic filtering. `DataInitializer` seeds 10 categories and 100 talks.
- **`ai/`** — `SummarizeAgent` wraps Spring AI `ChatClient` for streaming summarization (returns `Flux<String>`).
- **`ui/talks/`** — `TalkDetailsView` (grid + filter + form wiring), `DescriptionForm` (read-only textarea + summarize button + streaming summary display).

### Key Patterns

- **Server Push** — `@Push` on `TalkTrackerApplication` enables UI updates from background threads. AI streaming tokens are pushed via `ui.access()`.
- **Aura theme** — Uses `@StyleSheet(Aura.STYLESHEET)` (not Lumo).
- **Lazy loading** — Grid uses callback data provider with `VaadinSpringDataHelpers.toSpringPageRequest()`.
- **JPA Specification filtering** — `buildSpecification()` in `TalkDetailsView` creates a LIKE query across title, speaker, category name, language, and description.

## Testing

Tests use Vaadin's **browserless testing** (`SpringBrowserlessTest` from `browserless-test-junit6`). Tests extend `SpringBrowserlessTest` with `@SpringBootTest` and navigate to views via `navigate(ViewClass.class)`. View fields used in tests are package-private (not private).
