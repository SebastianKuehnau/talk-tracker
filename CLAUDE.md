# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Talk Tracker is a Spring Boot 4.0.5 + Vaadin 25.1 application for managing conference talks with AI-powered summarization via Ollama. It uses Java 21, H2 in-memory database, and Spring Data JPA.

## Build & Run Commands

```bash
# Run the application (dev mode)
./mvnw spring-boot:run

# Build (includes Vaadin frontend build)
./mvnw package

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=TalkDetailsViewTest

# Run a single test method
./mvnw test -Dtest=TalkDetailsViewTest#someMethod
```

## Tech Stack

- **Java 21** with Spring Boot 4.0.5
- **Vaadin 25.1** (Flow/server-side UI framework) with `vaadin-maven-plugin` for frontend builds
- **Spring AI 2.0.0-M3** with Ollama (`spring-ai-starter-model-ollama`, model: `qwen3.5:0.8b`)
- **Spring Data JPA** + **H2** (in-memory database)
- **Bean Validation** via `spring-boot-starter-validation`
- **Spring Actuator** for monitoring endpoints (health, info, metrics, prometheus)
- **Spring DevTools** for hot-reload during development
- **Vaadin Browserless Test (JUnit 6)** for UI unit testing without a browser

## Architecture

- Base package: `dev.workshop.vaadin.talktracker`
- Entry point: `TalkTrackerApplication` (standard `@SpringBootApplication`)
- Vaadin auto-launches browser on startup (`vaadin.launch-browser=true`)
- The project uses Maven wrapper (`mvnw`) -- no global Maven install needed
- Ollama must be running locally on `http://localhost:11434`

## Notes

- Package name uses underscores (`talktracker`) because the original hyphenated name is invalid in Java.
- H2 console is available at runtime via `spring-boot-h2console` dependency.
