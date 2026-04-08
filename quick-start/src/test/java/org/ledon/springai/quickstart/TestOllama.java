package org.ledon.springai.quickstart;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

/**
 * @author LeDon
 * @date 2026-04-09 00:55
 */
@SpringBootTest
public class TestOllama {
    @Test
    public void testOllama(@Autowired OllamaChatModel ollamaChatModel){
//        OllamaChatOptions
//        String call = ollamaChatModel.call("你好，你是谁？/no_think");
//        System.out.println(call);
        Flux<String> stream = ollamaChatModel.stream("你好，你是谁？");
        stream.toIterable().forEach(System.out::println);
    }

    @Test
    public void testMultimodality(@Autowired OllamaChatModel ollamaChatModel) {
        var imageResource = new ClassPathResource("files/1.png");

        OllamaChatOptions ollamaOptions = OllamaChatOptions.builder()
                .model("deepseek-r1:14b")
                .build();

        Media media = new Media(MimeTypeUtils.IMAGE_PNG, imageResource);


        ChatResponse response = ollamaChatModel.call(
                new Prompt(
                        UserMessage.builder().media(media)
                                .text("识别图片").build(),
                        ollamaOptions
                )
        );

        System.out.println(response.getResult().getOutput().getText());
    }
}
