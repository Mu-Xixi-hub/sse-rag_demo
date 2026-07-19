package com.example.sse.chat;

import java.time.Instant;

public record ChatMessage(long id, long sessionId, String content, boolean completed, Instant updatedAt) {}
