package org.ledon.springai;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootApplication
public class FlightBookingApplication {

    public static void main(String[] args) {

        SpringApplication.run(FlightBookingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(@Autowired VectorStore vectorStore,
                                        @Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs) {
        return args -> {
            vectorStore.write(                                  // 3.写入 + 向量化
                    new TokenTextSplitter().transform(          // 2.转换
                            new TextReader(termsOfServiceDocs).read())  // 1.读取
            );
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
            List<Document> documents = vectorStore.similaritySearch("退票的费用");
            System.out.println(documents);
        };
    }
}
