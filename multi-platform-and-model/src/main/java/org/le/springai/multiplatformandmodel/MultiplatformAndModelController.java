package org.le.springai.multiplatformandmodel;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;

/**
 * @author LeDon
 * @date 2026-04-09 23:02
 */
@RestController
public class MultiplatformAndModelController {

    HashMap<String, ChatModel> platforms = new HashMap<>();

    public MultiplatformAndModelController(
            DashScopeChatModel dashScopeChatModel,
            DeepSeekChatModel deepSeekChatModel,
            OllamaChatModel ollamaChatModel){
        platforms.put("dashscope", dashScopeChatModel);
        platforms.put("deepseek", deepSeekChatModel);
        platforms.put("ollama", ollamaChatModel);
    }

    @RequestMapping(value="/chat",produces = "text/stream;charset=UTF-8")
    public Flux<String> chat(
            String message,
            MultiPlatformAndModelOptions options) {
        String platform = options.getPlatform();
        ChatModel chatModel = platforms.get(platform);
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        ChatClient chatClient = builder.defaultOptions(
                ChatOptions.builder()
                        .temperature(options.getTemperature())
                        .model(options.getModel())
                        .build()
        ).build();

        return chatClient.prompt().user(message).stream().content();
    }
}
