package org.ledon.springai.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class EmbaddingTest {

    @Test
    public void testEmbadding(@Autowired OllamaEmbeddingModel
                                          ollamaEmbeddingModel) {

        float[] embedded = ollamaEmbeddingModel.embed("我叫LeDon");
        System.out.println(embedded.length);
        System.out.println(Arrays.toString(embedded));

    }

}