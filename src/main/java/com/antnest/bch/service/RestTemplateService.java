package com.antnest.bch.service;

import com.antnest.bch.dto.RawResponse;
import com.antnest.bch.dto.RequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Component
public class RestTemplateService<T> {

    @Autowired
    private RestTemplate restTemplate;

    String uri = "http://localhost:5001/bch/v1/block/transactions";
    String uri2 = "http://localhost:5001/bch/v1/block/latest";

    public RawResponse<T> callExchange(ParameterizedTypeReference<RawResponse<T>> responseType, RequestDto requestDto) {
        log.info("Start call method exchange");
        HttpHeaders headers = this.createHeaders();
        Map<String, JSONObject> requestObject = new HashMap<>();
        requestObject.put("request", new JSONObject(requestDto));
        HttpEntity<Map<String, String>> entity = new HttpEntity(requestObject.get("request").toString(), headers);
        RawResponse<T> exchange = restTemplate.exchange(uri, HttpMethod.POST, entity, responseType).getBody();
        log.info("Response call method post {}", exchange);
        return exchange;
    }

    public RawResponse<T> callExchangeNumber(ParameterizedTypeReference<RawResponse<T>> responseType) {
        log.info("Start call method exchange");
        HttpHeaders headers = this.createHeaders();
        Map<String, JSONObject> requestObject = new HashMap<>();
        requestObject.put("request", new JSONObject());
        HttpEntity<Map<String, String>> entity = new HttpEntity(requestObject.get("request").toString(), headers);
        RawResponse<T> exchange = restTemplate.exchange(uri2, HttpMethod.POST, entity, responseType).getBody();
        log.info("Response call method post {}", exchange);
        return exchange;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
