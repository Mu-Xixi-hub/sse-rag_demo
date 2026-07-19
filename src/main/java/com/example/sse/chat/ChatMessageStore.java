package com.example.sse.chat;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ChatMessageStore {
    private final AtomicLong ids = new AtomicLong();
    private final Map<Long, ChatMessage> messages = new ConcurrentHashMap<>();

    public ChatMessage createPlaceholder(long sessionId) {
        long id = ids.incrementAndGet();
        ChatMessage message = new ChatMessage(id, sessionId, "", false, Instant.now());
        messages.put(id, message);
        return message;
    }

    public void complete(long id, String content) {
        messages.computeIfPresent(id, (key, old) ->
            new ChatMessage(old.id(), old.sessionId(), content, true, Instant.now()));
    }

    public ChatMessage find(long id) {
        return messages.get(id);
    }
}
