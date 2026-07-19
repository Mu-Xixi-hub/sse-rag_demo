package com.example.sse.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DemoAnswerStreamService implements AnswerStreamService {

    private final ChatClient chatClient;

    public DemoAnswerStreamService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Flux<String> streamAnswer(long sessionId, String question) {

        return chatClient.prompt()
                .user(question)
                .stream()
                .content();
    }
}