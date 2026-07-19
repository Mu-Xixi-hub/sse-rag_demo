package com.example.sse.chat;

import reactor.core.publisher.Flux;

public interface AnswerStreamService {
    Flux<String> streamAnswer(long sessionId, String question);
}
