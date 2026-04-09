package dev.workshop.vaadin.talktracker.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SummarizeAgent {

    private final ChatClient chatClient;

    public SummarizeAgent(ChatModel chatModel) {
        chatClient = ChatClient.builder(chatModel).build();
    }

    public Flux<String> summarize(String description) {
        return chatClient.prompt()
                .system("Summarize the following talk description in 1 concise bullet point with the most important Buzzwords.")
                .user(description)
                .stream()
                .content();
    }
}

