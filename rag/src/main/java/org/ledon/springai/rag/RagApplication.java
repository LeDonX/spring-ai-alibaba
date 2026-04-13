package org.ledon.springai.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@SpringBootApplication
public class RagApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagApplication.class, args);
    }

    @Bean
    @Scope("prototype")
    RestClient.Builder restClientBuilder(RestClientBuilderConfigurer restClientBuilderConfigurer) {
        // 1. 先创建一个干净的 Builder
        RestClient.Builder builder = RestClient.builder();

        // 2. 先让 Spring Boot 注入它默认的那些能力（比如 JSON 转换器、拦截器等）
        restClientBuilderConfigurer.configure(builder);

        // 3. 使用 Spring Boot 3.4 的新 API 配置 360秒 超时
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.defaults() // 注意这里变成了小写的 defaults() 方法
                .withReadTimeout(Duration.ofSeconds(360))
                .withConnectTimeout(Duration.ofSeconds(360));

        // 4. 使用新的 FactoryBuilder 自动检测底层引擎并生成 Factory，强行塞给 Builder
        builder.requestFactory(ClientHttpRequestFactoryBuilder.detect().build(settings));

        return builder;
    }
}
