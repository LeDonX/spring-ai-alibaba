package org.ledon.springai.config;

import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LeDon
 */
@Configuration
public class RagConfig {

    /*@Bean
    public VectorStore vectorStore(OllamaEmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }*/
}
