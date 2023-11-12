package com.synchrony.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class ImgurConfig {

    @Value("${imgur.access.token}")
    private String accessToken;

    @Value("${imgur.service.connection.timeout}")
    private int connectionTimeout;

    @Value("${imgur.service.read.timeout}")
    private int readTimeout;

    @Bean
    public RestTemplate imgurRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(getHttpClientRequestFactory());
        restTemplate
                .getInterceptors()
                .add(
                        (request, body, execution) -> {
                            request.getHeaders().add("Authorization", "Bearer " + accessToken);
                            request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                            return execution.execute(request, body);
                        });
        return restTemplate;
    }

    private HttpComponentsClientHttpRequestFactory getHttpClientRequestFactory() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setConnectTimeout(connectionTimeout);
        httpComponentsClientHttpRequestFactory.setReadTimeout(readTimeout);
        return httpComponentsClientHttpRequestFactory;
    }
}
