package org.ledon.springai.multiplatformandmodel;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author LeDon
 * @date 2026-04-09 22:53
 */
@SpringBootTest
public class TestChatClient {

    @Test
    public void testChatClient(@Autowired OllamaChatModel ollamaChatModel) {
        ChatClient chatClient = ChatClient.builder(ollamaChatModel).build();
        String content = chatClient.prompt()
                .user("你好")
                .call()
                .content();
        System.out.println(content);
    }
}
