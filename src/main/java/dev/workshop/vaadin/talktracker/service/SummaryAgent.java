package dev.workshop.vaadin.talktracker.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

@Service
public class SummaryAgent {

    private final ChatClient chatClient;

    public SummaryAgent(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public String summarize(String text) {
        return chatClient.prompt()
                .system("Summarize the following talk description in 1 concise bullet point with the most important Buzzwords.")
                .user(text)
                .call()
                .content();
    }
}
