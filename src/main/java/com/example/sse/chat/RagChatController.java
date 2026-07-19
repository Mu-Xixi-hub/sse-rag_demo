package com.example.sse.chat;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/rag-chat")
public class RagChatController {
    private final AnswerStreamService answerService;
    private final ChatMessageStore messageStore;

    public RagChatController(AnswerStreamService answerService, ChatMessageStore messageStore) {
        this.answerService = answerService;
        this.messageStore = messageStore;
    }

    @PostMapping(value = "/sessions/{sessionId}/messages/stream",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(@PathVariable long sessionId,
                                                @RequestBody ChatRequest request) {
        if (request.question() == null || request.question().isBlank()) {
            return Flux.just(event("error", "问题不能为空"));
        }

        ChatMessage placeholder = messageStore.createPlaceholder(sessionId);
        StringBuilder fullContent = new StringBuilder();

        Flux<ServerSentEvent<String>> content = answerService.streamAnswer(sessionId, request.question())
            .doOnNext(fullContent::append)
            .map(chunk->event("chunk",escape(chunk)));

        return Flux.concat(
            Flux.just(event("start", String.valueOf(placeholder.id()))),
            content
        );
    }

    @GetMapping("/messages/{messageId}")
    public ChatMessage message(@PathVariable long messageId) {
        return messageStore.find(messageId);
    }

    private static ServerSentEvent<String> event(String name, String data) {
        return ServerSentEvent.<String>builder().event(name).data(data).build();
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\r", "\\r").replace("\n", "\\n");
    }

    private static String errorMessage(Throwable error) {
        return error instanceof TimeoutException
            ? "【错误】回答生成超时，请稍后重试。"
            : "【错误】回答生成失败：" + error.getMessage();
    }
}
