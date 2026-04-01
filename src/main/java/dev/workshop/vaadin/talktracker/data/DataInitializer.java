package dev.workshop.vaadin.talktracker.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TalkRepository talkRepository;

    public DataInitializer(CategoryRepository categoryRepository, TalkRepository talkRepository) {
        this.categoryRepository = categoryRepository;
        this.talkRepository = talkRepository;
    }

    @Override
    public void run(String... args) {
        Category frontend = createCategory("Frontend");
        Category security = createCategory("Security");
        Category ai = createCategory("AI");
        Category cloud = createCategory("Cloud");
        Category modernization = createCategory("Modernization");
        Category architecture = createCategory("Architecture");

        createTalk("Building Reactive UIs with Vaadin Flow",
                "This talk explores how Vaadin Flow enables developers to build modern, reactive user interfaces entirely in server-side Java. We will walk through the component model and demonstrate how data binding and event handling work without writing any JavaScript. Attendees will see live examples of complex forms, grids, and dashboards built with pure Java code. We also cover best practices for structuring large Vaadin applications into reusable components. The session includes a comparison of Vaadin Flow with traditional REST-plus-SPA approaches, highlighting trade-offs in developer productivity and runtime performance. Finally, we discuss how server push and reactive signals can bring real-time capabilities to your Vaadin apps.",
                "Anna Müller", "English", frontend);
        createTalk("Web Components in 2026",
                "Web Components have matured significantly over the past years, and 2026 marks a turning point for enterprise adoption. This session provides a comprehensive overview of the current ecosystem, including Shadow DOM, Custom Elements, and HTML Templates. We examine how major frameworks like Vaadin, Angular, and React interact with Web Components and where interoperability shines or breaks down. Practical examples show how to wrap third-party Web Components for use in Java-based server-side frameworks. Attendees will learn strategies for building a shared component library that works across teams using different frontend stacks. We also cover testing strategies, accessibility considerations, and performance profiling for Web Component-heavy applications.",
                "Liam Chen", "English", frontend);
        createTalk("CSS Container Queries for Responsive Layouts",
                "Las media queries han sido durante años la herramienta principal para el diseño responsivo, pero las container queries cambian las reglas del juego. En esta charla exploramos cómo las container queries permiten que los componentes se adapten al tamaño de su contenedor en lugar de al viewport. Veremos ejemplos prácticos de cómo refactorizar layouts existentes para aprovechar esta nueva capacidad. También analizamos la compatibilidad actual con los navegadores y las estrategias de fallback para entornos legacy. Los asistentes aprenderán a combinar container queries con CSS Grid y Flexbox para crear diseños verdaderamente modulares. Además, discutiremos cómo estas técnicas se integran con frameworks de componentes como Vaadin y Lit.",
                "Sofia Reyes", "Spanish", frontend);

        createTalk("Zero Trust Architecture in Practice",
                "Zero trust is no longer a buzzword — it is a fundamental shift in how we think about network security. This talk covers the core principles of zero trust and how they apply to microservice environments running on Kubernetes. We walk through implementing mutual TLS between services, policy-based access control with Open Policy Agent, and identity-aware proxies. Real-world case studies illustrate common pitfalls teams encounter when transitioning from perimeter-based security. Attendees will learn how to design service meshes that enforce zero trust without sacrificing developer velocity. We also discuss observability strategies that help detect policy violations and lateral movement attempts in production.",
                "James O'Brien", "English", security);
        createTalk("OAuth 2.1 and Spring Security 7",
                "OAuth 2.1 consolidates years of security best practices into a streamlined specification, and Spring Security 7 provides first-class support for it. This session explains the key changes from OAuth 2.0, including the removal of the implicit grant, mandatory PKCE for public clients, and stricter redirect URI matching. We demonstrate how to configure Spring Security 7 for both authorization servers and resource servers using the new APIs. Attendees will see practical examples of token exchange, refresh token rotation, and scope-based access control. The talk also covers integration with external identity providers like Keycloak and Okta. We conclude with a security checklist for production deployments that teams can adopt immediately.",
                "Priya Sharma", "English", security);
        createTalk("Supply Chain Security for Java Projects",
                "Los ataques a la cadena de suministro de software se han convertido en una de las amenazas más críticas para los proyectos Java. Esta charla analiza vectores de ataque reales, desde la inyección de dependencias maliciosas hasta la manipulación de artefactos en repositorios Maven. Exploraremos herramientas como Sigstore, SLSA y Dependabot para proteger el pipeline de build. Los asistentes aprenderán a configurar verificación de firmas en Maven y Gradle, y cómo implementar SBOMs para tener visibilidad completa de las dependencias. También discutiremos estrategias organizativas como el uso de repositorios proxy y políticas de actualización de dependencias. Casos de estudio de incidentes reales ilustran por qué cada equipo debe tomar la seguridad de la cadena de suministro en serio.",
                "Carlos Mendoza", "Spanish", security);

        createTalk("Spring AI: From Prototype to Production",
                "Building a prototype with Spring AI is easy, but taking it to production requires careful planning. This talk covers the full journey from a local Ollama experiment to a scalable, monitored AI service running in the cloud. We discuss prompt engineering patterns, token management, and cost optimization strategies for OpenAI and other commercial providers. Attendees will learn how to implement fallback chains that switch between providers based on latency, cost, or availability. The session also covers structured output parsing, function calling, and how to integrate AI responses into existing Spring Boot workflows. We share lessons learned from running Spring AI in production for six months, including common failure modes and how to handle them gracefully.",
                "Emily Watson", "English", ai);
        createTalk("RAG Patterns for Enterprise Search",
                "Retrieval-Augmented Generation combines the power of large language models with your organization's proprietary data. This talk presents battle-tested patterns for implementing RAG systems that actually work at enterprise scale. We cover document ingestion pipelines, chunking strategies, and vector database selection including pgvector, Weaviate, and Milvus. Attendees will learn how to evaluate retrieval quality using precision, recall, and NDCG metrics. The session demonstrates how to build a complete RAG pipeline using Spring AI with a real-world document corpus. We also address critical concerns like hallucination detection, source attribution, and access control for sensitive documents. Advanced topics include hybrid search combining keyword and semantic retrieval, and re-ranking strategies that dramatically improve answer quality.",
                "Marco Rossi", "English", ai);
        createTalk("Running LLMs Locally with Ollama",
                "No todas las organizaciones pueden enviar sus datos a APIs externas, lo que convierte a la ejecución local de LLMs en una necesidad práctica. Esta charla es una guía práctica para ejecutar y ajustar modelos de lenguaje en tu propio hardware usando Ollama. Exploraremos la instalación, configuración y los requisitos de hardware para diferentes tamaños de modelo, desde 7B hasta 70B parámetros. Los asistentes verán demostraciones en vivo de inferencia local con tiempos de respuesta sorprendentemente rápidos en hardware de consumo. También cubrimos técnicas de cuantización que reducen los requisitos de memoria sin sacrificar demasiada calidad. Discutiremos cómo integrar Ollama con Spring AI para crear aplicaciones Java que funcionan completamente offline. Finalmente, comparamos el rendimiento y la calidad de varios modelos abiertos como Llama, Mistral y Gemma.",
                "Alejandro Torres", "Spanish", ai);
        createTalk("AI-Assisted Code Review",
                "Code reviews are essential but time-consuming, and AI tools can help without replacing the human reviewer. This talk explores practical ways to integrate AI into your code review workflow using both commercial APIs and local models. We demonstrate automated detection of common issues like security vulnerabilities, performance anti-patterns, and style inconsistencies. Attendees will see how to build custom review bots that understand your team's specific conventions and architecture decisions. The session covers prompt engineering techniques that produce actionable review comments rather than vague suggestions. We also discuss the psychology of AI-assisted reviews and how to maintain team trust when machines are involved in quality gates. Real metrics from teams that adopted AI-assisted reviews show measurable improvements in defect detection rates and review turnaround times.",
                "Sarah Kim", "English", ai);

        createTalk("Kubernetes Operators in Java",
                "Kubernetes operators extend the platform with domain-specific automation, and Java developers can now build them without learning Go. This talk introduces the Java Operator SDK and walks through building a production-ready operator from scratch. We cover custom resource definitions, reconciliation loops, and status management with practical code examples. Attendees will learn how to handle edge cases like concurrent modifications, finalizers, and leader election in multi-replica deployments. The session demonstrates testing strategies including unit tests with mock Kubernetes APIs and integration tests with kind clusters. We also discuss when an operator is the right solution versus simpler alternatives like Helm charts or Kustomize overlays. Performance benchmarking results show how Java operators compare to Go-based alternatives in terms of memory usage and reconciliation latency.",
                "David Park", "English", cloud);
        createTalk("Serverless Java with GraalVM Native Image",
                "Cold start times have long been Java's Achilles heel in serverless environments, but GraalVM Native Image changes everything. This session shows how to achieve sub-100ms cold starts for Java functions running on AWS Lambda, Azure Functions, and Google Cloud Functions. We walk through the native image compilation process, common pitfalls with reflection and dynamic proxies, and how Spring Boot 4 simplifies native builds with ahead-of-time processing. Attendees will see benchmarks comparing JVM versus native performance across different workload types. The talk covers strategies for managing native image build times in CI/CD pipelines and how to debug issues that only appear in native mode. We also discuss the trade-offs between native images and CRaC-based approaches for reducing startup time while keeping the JVM's runtime optimizations.",
                "Nina Petrov", "English", cloud);
        createTalk("Multi-Cloud Strategies That Actually Work",
                "La estrategia multi-cloud suena atractiva en las presentaciones ejecutivas, pero la realidad de operar cargas de trabajo en múltiples proveedores es mucho más compleja. Esta charla comparte lecciones aprendidas de equipos que ejecutan servicios de producción en AWS, Azure y GCP simultáneamente. Analizamos los patrones que funcionan, como el uso de Kubernetes como capa de abstracción y Terraform para infraestructura declarativa. También discutimos los antipatrones más comunes, incluyendo intentar usar el mínimo común denominador de cada proveedor. Los asistentes aprenderán estrategias prácticas para gestionar redes, identidades y observabilidad en un entorno multi-cloud. Cubrimos los costos ocultos que los equipos suelen descubrir demasiado tarde, como el tráfico de egress y las diferencias en modelos de facturación. Finalmente, presentamos un framework de decisión para evaluar cuándo multi-cloud realmente aporta valor y cuándo es mejor quedarse con un solo proveedor.",
                "Lucas Fernández", "Spanish", cloud);

        createTalk("Migrating from Spring Boot 2 to 4",
                "Migrating a large application from Spring Boot 2 to 4 is a significant undertaking that touches nearly every layer of your codebase. This talk provides a practical, step-by-step migration guide based on real-world experience with multiple enterprise applications. We cover the transition from javax to jakarta namespaces, removed and deprecated APIs, and configuration property changes that break silently. Attendees will learn how to use OpenRewrite recipes to automate the most tedious parts of the migration. The session addresses common blockers like incompatible third-party libraries and how to find or create replacements. We also discuss testing strategies that give you confidence during the migration, including how to run tests against both versions simultaneously. A detailed timeline from a real migration project helps teams estimate effort and plan their own transition.",
                "Thomas Berg", "English", modernization);
        createTalk("Strangler Fig Pattern for Legacy Modernization",
                "Rewriting a legacy system from scratch is risky and often fails, but the Strangler Fig pattern offers a proven alternative. This talk explains how to incrementally replace a monolith by routing requests through a facade that gradually shifts traffic to new services. We demonstrate practical implementation using Spring Cloud Gateway as the routing layer between legacy and modern components. Attendees will see how to identify the right starting points for strangulation based on business value and technical risk. The session covers data synchronization challenges when old and new systems need to share state during the transition period. We discuss organizational strategies for running dual systems, including how to maintain team morale during what can be a multi-year effort. Real project metrics show how one team reduced their legacy codebase by 70% over 18 months while maintaining zero downtime.",
                "Rachel Adams", "English", modernization);
        createTalk("From Java 11 to 21: Features You Should Adopt Now",
                "Java has evolved dramatically since version 11, and many teams are not yet leveraging the most impactful features available in Java 21. This talk cuts through the noise and identifies which features deliver the most value for typical enterprise applications. We start with virtual threads and demonstrate how they simplify concurrent programming while dramatically improving throughput for I/O-bound workloads. Pattern matching with sealed types enables more expressive and safer code, and we show refactoring examples from real codebases. Records and compact constructors reduce boilerplate in data transfer objects and domain models. The session also covers text blocks, switch expressions, and the new SequencedCollection interfaces that make everyday coding more pleasant. We conclude with features you should skip or defer, explaining why not every shiny new addition belongs in production code right away.",
                "Kenji Tanaka", "English", modernization);
        createTalk("Modernizing Database Access with jOOQ",
                "Muchos proyectos Java todavía utilizan JDBC plano o ORMs desactualizados que generan consultas ineficientes y dificultan el mantenimiento. Esta charla presenta jOOQ como una alternativa moderna que combina SQL tipado con la potencia del generador de código. Exploraremos cómo jOOQ genera clases a partir del esquema de base de datos, proporcionando autocompletado y verificación en tiempo de compilación para todas las consultas. Los asistentes verán cómo migrar gradualmente desde JPA o JDBC hacia jOOQ sin necesidad de reescribir todo de una vez. Cubrimos patrones avanzados como consultas dinámicas, funciones de ventana y CTEs recursivos que son difíciles de expresar con ORMs tradicionales. También discutimos la integración con Spring Boot y cómo configurar la generación de código en el pipeline de CI/CD. Finalmente, presentamos benchmarks que comparan el rendimiento de jOOQ con Hibernate en escenarios de lectura intensiva típicos de aplicaciones empresariales.",
                "Isabel García", "Spanish", modernization);

        createTalk("Hexagonal Architecture with Spring Boot",
                "Hexagonal architecture, also known as ports and adapters, is a powerful pattern for building applications that are easy to test and maintain over time. This talk explains the core concepts and demonstrates how to implement them cleanly in a Spring Boot application. We define clear boundaries between domain logic, application services, and infrastructure adapters using Java interfaces and dependency injection. Attendees will see how this architecture makes it trivial to swap database implementations, replace messaging systems, or add new delivery mechanisms like GraphQL alongside REST. The session includes a live coding exercise where we refactor a typical layered Spring Boot application into hexagonal architecture. We discuss common mistakes like leaking framework annotations into the domain layer and how to prevent them. Testing becomes dramatically simpler because domain logic can be tested in isolation with plain unit tests, without Spring context or database dependencies.",
                "Michael Scott", "English", architecture);
        createTalk("Event-Driven Systems with Kafka and Spring",
                "Event-driven architectures enable loosely coupled systems that scale independently and recover gracefully from failures. This talk provides a comprehensive guide to designing and implementing event-driven systems using Apache Kafka and Spring Cloud Stream. We cover event modeling, topic design, and partitioning strategies that ensure ordered processing where it matters. Attendees will learn about the different messaging patterns — event notification, event-carried state transfer, and event sourcing — and when to use each one. The session demonstrates how to implement the outbox pattern to ensure reliable event publishing alongside database transactions. We also address operational concerns like schema evolution with Avro and the Schema Registry, consumer group management, and dead letter queues for handling poison messages. Performance tuning tips based on production experience help teams avoid common bottlenecks in high-throughput scenarios.",
                "Olivia Johnson", "English", architecture);
        createTalk("Modular Monoliths: The Best of Both Worlds",
                "Microservices have become the default architectural choice, but they introduce significant operational complexity that many teams are not prepared for. This talk makes the case for modular monoliths as a pragmatic alternative that delivers most of the benefits of microservices without the distributed systems headaches. We demonstrate how to structure a Spring Boot application into well-defined modules with explicit boundaries and minimal coupling. Attendees will learn how to enforce module boundaries using ArchUnit tests and Java module system features. The session covers strategies for shared data access, including when modules should share a database and when they need their own schemas. We discuss how a well-structured monolith can be selectively decomposed into services later if and when the need actually arises. Real-world examples from teams that migrated from microservices back to a modular monolith illustrate that simpler is often better.",
                "Henrik Larsson", "English", architecture);
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private void createTalk(String title, String description, String speaker, String language, Category category) {
        Talk talk = new Talk();
        talk.setTitle(title);
        talk.setDescription(description);
        talk.setSpeaker(speaker);
        talk.setLanguage(language);
        talk.setCategory(category);
        talkRepository.save(talk);
    }
}