package com.eveb.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ElasticSearchConnection extends Connection{

    @Value("${elasticsearch.url}")
    private  String url;
    @Value("${elasticsearch.port}")
    private int clientport;
    @Value("${elasticsearch.rest.port}")
    private int restport;
    @Value("${elasticsearch.name}")
    private String name;
    @Value("${elasticsearch.password}")
    private String password;
    @Value("${elasticsearch.timeout}")
    private int timeout;

    public RestClient restClient;

    public ElasticSearchConnection() {

    }

    @PostConstruct
    private void init() {
        restClient = initRestClient(url, restport, name, password, timeout);
    }



}
