package com.binlicoder.houtai.controller;

import com.binlicoder.houtai.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StatsController {
    private final RestClient client;
    private final String identityUrl;
    private final String academicUrl;
    public StatsController(@Value("${services.identity-url}") String identityUrl,
                           @Value("${services.academic-url}") String academicUrl) {
        this.identityUrl = identityUrl;
        this.academicUrl = academicUrl;
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(1000);
        this.client = RestClient.builder().requestFactory(factory).build();
    }
    @GetMapping("/api/stats")
    public Result<Map<String, Integer>> counts() {
        try {
            Map<String, Integer> counts = new HashMap<>(fetch(identityUrl));
            counts.putAll(fetch(academicUrl));
            return Result.success(counts);
        } catch (RestClientException | IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "统计依赖服务不可用", exception);
        }
    }
    private Map<String, Integer> fetch(String url) {
        Result<Map<String, Integer>> response = client.get().uri(url + "/api/stats")
                .retrieve().body(new ParameterizedTypeReference<Result<Map<String, Integer>>>() {});
        if (response == null || !Integer.valueOf(200).equals(response.getCode()) || response.getData() == null) {
            throw new IllegalStateException("Invalid statistics response");
        }
        return response.getData();
    }
}
