# Talk Tracker

A Spring Boot + Vaadin application for managing conference talks with AI-powered summarization.

## What does this project do?

Talk Tracker is a web application for managing conference talks:

- **View & filter talks** — Grid with lazy loading and real-time text filter (title, speaker, description, language, category)
- **AI summarization** — Streaming summarization of talk descriptions using OpenAI via Spring AI

## Tech Stack

| Technology | Version         |
|---|-----------------|
| Java | 25              |
| Spring Boot | 4.0.5           |
| Vaadin (Flow) | 25.1.1          |
| Spring AI (OpenAI) | 2.0.0-M4        |
| Database | H2 (in-memory)  |
| Build | Maven (Wrapper) |

## Prerequisites

- **Java 25** (JDK) — e.g. via [SDKMAN](https://sdkman.io/): `sdk install java 25-tem`
- **OpenAI API Key** — required for the AI summarization feature

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

The browser opens automatically. If not: [http://localhost:8080](http://localhost:8080)

### 4. Run tests

```bash
# All tests
./mvnw test

# Single test class
./mvnw test -Dtest=TalkDetailsViewTest
```

## Project Structure

```
src/main/java/dev/workshop/vaadin/talktracker/
├── TalkTrackerApplication.java         # Entry point (@Push, Aura theme)
├── data/
│   ├── Talk.java                       # Entity: title, description, speaker, language, category
│   ├── Category.java                   # Entity: name
│   ├── TalkRepository.java            # JPA Repository + JpaSpecificationExecutor
│   ├── CategoryRepository.java        # JPA Repository
│   └── DataInitializer.java           # Seeds 10 categories & 100 sample talks
├── ai/
│   └── SummaryAgent.java              # Spring AI ChatClient (streaming)
└── ui/
    ├── MainLayout.java                # AppLayout with side navigation
    └── talks/
        ├── TalkDetailsView.java       # Main view (grid + form, JPA Specification filter)
        └── TalkForm.java             # Read-only form with AI summarize button
```

## Notes

- The H2 database is in-memory — data is lost on restart. 10 categories and 100 sample talks are seeded automatically on startup.
- Without a valid `OPENAI_API_KEY`, the summarization feature won't work, but the rest of the app runs normally.