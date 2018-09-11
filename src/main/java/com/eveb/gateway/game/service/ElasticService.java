package com.eveb.gateway.game.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.config.ElasticSearchConnection;
import com.eveb.gateway.config.ElasticSearchConnectionRead;
import com.eveb.gateway.game.model.RequestLog;
import com.eveb.gateway.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Slf4j
@Service
public class ElasticService {

    @Autowired
    private ElasticSearchConnection conn;
    @Autowired
    private ElasticSearchConnectionRead read_conn;

    /***
     * 插入ElasticSearch
     * @param object
     * @throws Exception
     */
    public void insert(Object object){
        try {
            HttpEntity entity = new NStringEntity(JSON.toJSONStringWithDateFormat(object,
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), ContentType.APPLICATION_JSON);
            conn.restClient.performRequest("POST",
                    "api_gateway" + "/" + "requestLog" + "/"
                            + new SnowFlake().nextId(), Collections.singletonMap("pretty", "true"), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void insert(String ip, String agentAccount, String platform, String apiName, String parameter, Date time){
//        RequestLog log=new RequestLog(ip,agentAccount,platform,apiName,parameter,time);
//        try {
//            HttpEntity entity = new NStringEntity(JSON.toJSONStringWithDateFormat(log,
//                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), ContentType.APPLICATION_JSON);
//            conn.restClient.performRequest("POST",
//                    "api_gateway" + "/" + "requestLog" + "/"
//                            + new SnowFlake().nextId(), Collections.singletonMap("pretty", "true"), entity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
