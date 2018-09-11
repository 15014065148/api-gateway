package com.eveb.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ElasticSearchConnectionRead extends Connection {

    @Value("${read_elasticsearch.url}")
    private String url;
    @Value("${read_elasticsearch.port}")
    private int clientport;
    @Value("${read_elasticsearch.rest.port}")
    private int restport;
    @Value("${read_elasticsearch.name}")
    private String name;
    @Value("${read_elasticsearch.password}")
    private String password;
    @Value("${read_elasticsearch.timeout}")
    private int timeout;

    public RestClient restClient_Read;

    public ElasticSearchConnectionRead() {

    }

    @PostConstruct
    private void init() {
        restClient_Read = initRestClient(url, restport, name, password, timeout);
    }

}
