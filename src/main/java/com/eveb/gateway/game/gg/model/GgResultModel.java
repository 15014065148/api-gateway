package com.eveb.gateway.game.gg.model;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.game.unity.model.UnityResultModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/2 15:25
 **/
@Slf4j
public class GgResultModel {

    private static final Integer CODE_SUCC=0;
    private static final String MAP_CODE="code";
    private static final String MAP_MSG="msg";
    private static final String MAP_URL="url";
    private static final String MAP_BALANCE="dbalance";

    public static UnityResultModel postUnityResult(String rs) {
        Map map=JSON.parseObject(rs);
        if(CODE_SUCC.equals(map.get(MAP_CODE))){
           return new UnityResultModel(Boolean.TRUE,map.get(MAP_BALANCE) == null ? null : map.get(MAP_BALANCE));
        }
        return new UnityResultModel(Boolean.FALSE,map.get(MAP_MSG).toString());
    }

    public static UnityResultModel playGameUnityResult(String rs) {
        Map map=JSON.parseObject(rs);
        if(CODE_SUCC.equals(map.get(MAP_CODE))){
            return new UnityResultModel(Boolean.TRUE,map.get(MAP_URL) == null ? null : map.get(MAP_URL));
        }
        return new UnityResultModel(Boolean.FALSE,map.get(MAP_MSG).toString());
    }

}
