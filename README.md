# Talk Tracker

A Spring Boot + Vaadin application for browsing JAX 2026 conference talks with a conversational AI assistant.

## What does this project do?

Talk Tracker displays the real JAX 2026 conference schedule and lets you explore it through natural language:

- **Browse talks** — Grid with title, speaker, room, date, time, and tracks
- **Conversational AI filter** — Type queries like "Show me morning workshops on Monday" and the AI filters the grid via tool-calling (Spring AI + OpenAI)

## Tech Stack

| Technology | Version         |
|---|-----------------|
| Java | 25              |
| Spring Boot | 4.0.6           |
| Vaadin (Flow) | 25.1.4          |
| Spring AI (OpenAI) | 2.0.0-M5        |
| Database | H2 (in-memory)  |
| Build | Maven (Wrapper) |

## Prerequisites

- **Java 25** (JDK) — e.g. via [SDKMAN](https://sdkman.io/): `sdk install java 25-tem`
- **OpenAI API Key** — required for the AI chat feature

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd talk-tracker
```

### 2. Set the OpenAI API Key

```bash
export OPENAI_API_KEY=sk-...
```

Alternatively, set it directly in `src/main/resources/application.properties` (don't commit this!).

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) in your browser.

### 4. Run tests

```bash
# All tests
./mvnw test

# Single test class
./mvnw test -Dtest=TalkDetailViewTest
```

## Project Structure

```
src/main/java/dev/workshop/vaadin/talktracker/
├── TalkTrackerApplication.java         # Entry point (@Push, @PWA, Aura theme)
├── data/
│   ├── Talk.java                       # Entity: title, speaker, tracks, format, date/time, room
│   ├── TalkRepository.java             # JPA Repository + JpaSpecificationExecutor + @EntityGraph
│   └── DataInitializer.java           # Seeds real JAX 2026 schedule (5 days, May 4–8 2026)
└── ui/
    └── talks/
        └── TalkListView.java          # Main view: Grid + chat UI + AI tool-calling logic
```

## Notes

- The H2 database is in-memory — data resets on restart. The JAX 2026 schedule is re-seeded automatically.
- Without a valid `OPENAI_API_KEY`, the AI chat won't respond, but the grid still shows all talks.
- The AI uses tool-calling: it invokes `getAllTalks()` to read the schedule and `filterTalks(ids)` to update the grid — no keyword search, pure natural language.