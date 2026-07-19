package com.example.sse.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RagChatControllerTest {
    @Autowired MockMvc mvc;

    @Test
    void shouldStartSseStream() throws Exception {
        mvc.perform(post("/api/rag-chat/sessions/1/messages/stream")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .content("{\"question\":\"测试换行\"}"))
            .andExpect(request().asyncStarted())
            .andExpect(status().isOk());
    }
}
