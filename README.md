# Talk Tracker

A Spring Boot + Vaadin application for managing conference talks with AI-powered summarization and live registration tracking.

## What does this project do?

Talk Tracker is a web application for managing conference talks:

- **View & filter talks** — Grid with lazy loading and real-time text filter (title, speaker, description, language, category)
- **Create, edit & delete talks** — Form with Bean Validation
- **AI summarization** — Summarize talk descriptions into a single sentence using Ollama (local LLM)
- **Live registrations** — Simulated attendee registrations updating every 300ms via Vaadin Push

## Tech Stack

| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.5 |
| Vaadin (Flow) | 25.1 |
| Spring AI (Ollama) | 2.0.0-M3 |
| Database | H2 (in-memory) |
| Build | Maven (Wrapper) |

## Prerequisites

- **Java 21** (JDK) — e.g. via [SDKMAN](https://sdkman.io/): `sdk install java 21-tem`
- **Ollama** — required for the AI summarization feature ([Install Ollama](https://ollama.com/download))

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd talk-tracker
```

### 2. Install and start Ollama

```bash
# Install Ollama (macOS)
brew install ollama

# Start the Ollama server
ollama serve

# Pull the required model
ollama pull qwen3.5:0.8b
```

The app expects Ollama running on `http://localhost:11434` with the `qwen3.5:0.8b` model. You can change the model in `application.properties`.

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
├── TalkTrackerApplication.java         # Entry point
├── data/
│   ├── Talk.java                       # Entity: conference talk
│   ├── Category.java                   # Entity: category
│   ├── TalkRepository.java            # JPA Repository
│   ├── CategoryRepository.java        # JPA Repository
│   └── DataInitializer.java           # 20 sample talks & 6 categories
├── service/
│   ├── SummaryAgent.java              # Spring AI ChatClient
│   └── RegistrationMockService.java   # Simulated live registrations
└── ui/
    ├── MainLayout.java                # AppLayout with navigation
    └── talks/
        ├── TalkDetailsView.java       # Main view (grid + form)
        ├── TalkGrid.java              # Grid component
        └── TalkForm.java             # Form component
```

## Notes

- The H2 database is in-memory — data is lost on restart. 20 sample talks are created automatically on startup.
- Without a running Ollama instance, the summarization feature won't work, but the rest of the app runs normally.
- Actuator endpoints are available at `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`.
