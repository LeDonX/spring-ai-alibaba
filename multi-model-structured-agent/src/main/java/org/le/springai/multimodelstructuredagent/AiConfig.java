package org.le.springai.multimodelstructuredagent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.model.ollama.autoconfigure.OllamaChatProperties;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AiConfig {

    @Bean
    public ChatClient planningChatClient(OllamaChatModel chatModel,
                                         OllamaChatProperties options,
                                         ChatMemory chatMemory) {
        OllamaChatOptions ollamaChatOptions = OllamaChatOptions.fromOptions(options.getOptions());
        ollamaChatOptions.setTemperature(0.7);

        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        # 票务助手任务拆分规则
                        ## 1.要求
                        ### 1.1 根据用户内容识别任务
                        
                        ## 2. 任务
                        ### 2.1 JobType:退票(CANCEL) 要求用户提供姓名和预定号， 或者从对话中提取；
                        ### 2.2 JobType:查票(QUERY) 要求用户提供预定号， 或者从对话中提取；
                        ### 2.3 JobType:其他(OTHER)
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultOptions(ollamaChatOptions)
                .build();
    }

    @Bean
    public ChatClient botChatClient(OllamaChatModel chatModel,
                                    OllamaChatProperties options,
                                    ChatMemory chatMemory) {

        OllamaChatOptions ollamaChatOptions = OllamaChatOptions.fromOptions(options.getOptions());
        ollamaChatOptions.setTemperature(1.2);
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        你是le航空智能客服代理，请以友好的语气服务用户。
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultOptions(ollamaChatOptions)
                .build();
    }
}