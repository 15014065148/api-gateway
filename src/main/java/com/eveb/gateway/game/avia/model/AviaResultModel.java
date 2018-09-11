package com.eveb.gateway.game.avia.model;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.game.unity.model.UnityResultModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/17 10:38
 **/
@Slf4j
public class AviaResultModel {

    private static final Integer SUCCESS_ERR=0;
    private static final Integer SUCCESS_SUC=1;
    private static final String TRANSFER_NONE="None";
    private static final String TRANSFER_FINISH="Finish";
    private static final String MAP_SUCCESS="success";
    private static final String TRANSFER_NONE_MSG="处理中";

    public static UnityResultModel registerUnityResult(String rs){
        Map rsMap=JSON.parseObject(rs);
        if(SUCCESS_SUC.equals(rsMap.get(MAP_SUCCESS))){
            return new UnityResultModel(Boolean.TRUE,null);
        }
        return new UnityResultModel(Boolean.FALSE,rsMap.get("msg"));
    }

    public static UnityResultModel loginUnityResult(String rs){
        log.info("");
        Map rsMap=JSON.parseObject(rs);
        if(SUCCESS_SUC.equals(rsMap.get(MAP_SUCCESS))){
            return new UnityResultModel(Boolean.TRUE,((Map)rsMap.get("info")).get("Url"));
        }
        return new UnityResultModel(Boolean.FALSE,rsMap.get("msg"));
    }

    public static UnityResultModel transferUnityResult(String rs){
        log.info("");
        Map rsMap=JSON.parseObject(rs);
        if(SUCCESS_SUC.equals(rsMap.get(MAP_SUCCESS))){
            return new UnityResultModel(Boolean.TRUE,((Map)rsMap.get("info")).get("ID"));
        }
        return new UnityResultModel(Boolean.FALSE,rsMap.get("msg"));
    }

    public static UnityResultModel transferStatusUnityResult(String rs){
        log.info("");
        Map rsMap=JSON.parseObject(rs);
        if(SUCCESS_SUC.equals(rsMap.get(MAP_SUCCESS))){
           Map infoMap= (Map)rsMap.get("info");
           if(TRANSFER_FINISH.equals(infoMap.get("Status"))) {
               return new UnityResultModel(Boolean.TRUE, ((Map) rsMap.get("info")).get("ID"));
           }
            return new UnityResultModel(Boolean.FALSE,TRANSFER_NONE_MSG);
        }
        return new UnityResultModel(Boolean.FALSE,rsMap.get("msg"));
    }

    public static UnityResultModel balanceUnityResult(String rs){
        log.info("");
        Map rsMap=JSON.parseObject(rs);
        if(SUCCESS_SUC.equals(rsMap.get(MAP_SUCCESS))){
            return new UnityResultModel(Boolean.TRUE,((Map)rsMap.get("info")).get("Money"));
        }
        return new UnityResultModel(Boolean.FALSE,rsMap.get("msg"));
    }

}
