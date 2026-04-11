package org.ledon.springai.chatclient;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author LeDon
 * @date 2026-04-10 00:27
 */
@SpringBootTest
public class TestAdvisor {
    @Test
    public void testChatClient(@Autowired
                               ChatClient.Builder chatClientBuilder) {
        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        String content = chatClient.prompt()
                .user("你好")
//                .advisors()
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void testReReadingAdvisor(@Autowired
                                     ChatClient.Builder chatClientBuilder) {
        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor(),new ReReadingAdvisor())
                .build();
        String content = chatClient.prompt()
                .user("蝙蝠侠帅不帅")
//                .advisors()
                .call()
                .content();
        System.out.println(content);
    }
}
