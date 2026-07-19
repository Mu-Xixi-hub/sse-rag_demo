package com.example.sse.chat;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class DemoAnswerStreamService implements AnswerStreamService {
    @Override
    public Flux<String> streamAnswer(long sessionId, String question) {
        String answer = "你问的是：“" + question + "”。\n\n"
            + "这是后端按字符增量推送的演示。接入 Spring AI 后，只需把这个模拟 Flux 替换为：\n"
            + "chatClient.prompt().user(question).stream().content()";
        return Flux.fromStream(answer.codePoints().mapToObj(cp -> new String(Character.toChars(cp))))
            .delayElements(Duration.ofMillis(35));
    }
}
