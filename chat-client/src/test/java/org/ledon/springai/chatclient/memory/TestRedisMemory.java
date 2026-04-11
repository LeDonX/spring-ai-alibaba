package org.ledon.springai.chatclient.memory;

import com.alibaba.cloud.ai.memory.redis.RedissonRedisChatMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ledon.springai.chatclient.ReReadingAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author LeDon
 * @date 2026-04-10 22:36
 */
@SpringBootTest
public class TestRedisMemory {

    ChatClient chatClient;

    @BeforeEach
    public  void init(@Autowired OllamaChatModel chatModel,
                      @Autowired ChatMemory chatMemory) {
        chatClient = ChatClient
                .builder(chatModel)
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @Test
    public void testChatOptions() {
        String content = chatClient.prompt()
                .user("你好，我叫LeDon！")
                .advisors(new ReReadingAdvisor())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,"1"))
                .call()
                .content();
        System.out.println(content);
        System.out.println("--------------------------------------------------------------------------");

        content = chatClient.prompt()
                .user("我叫什么 ？")
                .advisors(new ReReadingAdvisor())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,"1"))
                .call()
                .content();
        System.out.println(content);
    }
    @TestConfiguration
    static class RedisMemoryConfig {

        @Value("${spring.ai.memory.redis.host}")
        private String redisHost;
        @Value("${spring.ai.memory.redis.port}")
        private int redisPort;
        @Value("${spring.ai.memory.redis.password}")
        private String redisPassword;
        @Value("${spring.ai.memory.redis.timeout}")
        private int redisTimeout;

        @Bean
        public RedissonRedisChatMemoryRepository redisChatMemoryRepository() {
            return RedissonRedisChatMemoryRepository.builder()
                    .host(redisHost)
                    .port(redisPort)
                    // 若没有设置密码则注释该项
                    // .password(redisPassword)
                    .timeout(redisTimeout)
                    .build();
        }
        @Bean
        ChatMemory chatMemory(RedissonRedisChatMemoryRepository chatMemoryRepository) {
            return MessageWindowChatMemory
                    .builder()
                    .maxMessages(1)
                    .chatMemoryRepository(chatMemoryRepository).build();
        }
    }
}
