package org.ledon.springai.chatclient;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

/**
 * @author LeDon
 * @date 2026-04-09 23:51
 */
@SpringBootTest
public class TestPrompt {

    @Test
    public void testSystemPrompt(@Autowired ChatClient.Builder chatClientBuilder,
                                 @Value("classpath:/files/prompt.st")
                                 Resource systemResource) {
        ChatClient chatClient = chatClientBuilder
                .defaultSystem(systemResource)
                .build();
        String content = chatClient
                .prompt()
                .system(p->p.param("name","LeDon").param("age","18").param("sex","男"))
                .user("你好")
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void testSystemPrompt2(@Autowired ChatClient.Builder chatClientBuilder) {
        ChatClient chatClient = chatClientBuilder
                .build();
        String content = chatClient
                .prompt()
//                .system(p->p.param("name","LeDon").param("age","18").param("sex","男"))
                .user(u->u.text("""
                        # 角色说明
                        你是一名专业法律顾问AI……
                        
                        ## 回复格式
                        1. 问题分析
                        2. 相关依据
                        3. 梳理和建议
                        
                        **特别注意：**
                        - 不承担律师责任。
                        - 不生成涉敏、虚假内容。
                        
                        回答用户的法律咨询问题
                        
                        {question}
                        """).param("question","被裁的补偿金"))
                .call()
                .content();
        System.out.println(content);
    }


}