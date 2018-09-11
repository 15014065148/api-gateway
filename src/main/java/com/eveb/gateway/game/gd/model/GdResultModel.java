package com.eveb.gateway.game.gd.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.unity.model.UnityResultModel;
import com.eveb.gateway.utils.XmlOnMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class GdResultModel {

    public UnityResultModel getCreatePlayerResult(String resultXml) {
        return getUnityResultModel(resultXml);
    }

    public UnityResultModel getLogoutResult(String resultXml) {
        return getUnityResultModel(resultXml);
    }

    public UnityResultModel getDepositResult(String resultXml) {
        return getUnityResultModel(resultXml);
    }

    public UnityResultModel getWithdrawalResult(String resultXml) {
        return getUnityResultModel(resultXml);
    }

    public UnityResultModel getQueryBalanceResult(String resultXml) {
        Map<String, Object> map = xmlDataAnalysis(resultXml);
        if (map.get("errorCode").toString().trim().equals("0")) {
            return new UnityResultModel(true, map.get("balance"));
        } else {
            return new UnityResultModel(false, GdConstants.ErrEnum.getMsg(map.get("errorCode").toString().trim()));
        }
    }

    public UnityResultModel getCheckTransferResult(String resultXml) {
        return getUnityResultModel(resultXml);
    }

    public UnityResultModel getPlayGameResult(String resultXml) {
        if (!StringUtils.isEmpty(resultXml)) {
            return new UnityResultModel(true, resultXml);
        } else {
            return new UnityResultModel(false, ApplicationConstants.FAIL);
        }
    }

    private UnityResultModel getUnityResultModel(String resultXml) {
        Map<String, Object> map = xmlDataAnalysis(resultXml);
        if (map.get("errorCode").toString().trim().equals("0")) {
            return new UnityResultModel(true, ApplicationConstants.SUCCESS);
        } else {
            return new UnityResultModel(false, GdConstants.ErrEnum.getMsg(map.get("errorCode").toString().trim()));
        }
    }


    private Map<String, Object> xmlDataAnalysis(String resultXml) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = XmlOnMap.xml2JSON(resultXml);
        Map mapXml = JSON.parseObject(jsonObject.toJSONString(), Map.class);
        Map mapReply = JSON.parseObject(mapXml.get("Reply").toString(), Map.class);
        String errorCode = ((Map) JSON.parse(JSON.parseArray(mapReply.get("Header").toString(), Map.class).get(0).toString().replace("=", ":"))).get("ErrorCode").toString().replace("[\"", "").replace("\"]", "");
        map.put("errorCode", errorCode);
        try {
            String balance = ((Map) JSON.parse(JSON.parseArray(mapReply.get("Param").toString(), Map.class).get(0).toString().replace("=", ":"))).get("Balance").toString().replace("[\"", "").replace("\"]", "");
            map.put("balance", new BigDecimal(balance));
        } catch (Exception e) {
            map.put("balance", null);
        }
        return map;
    }

}
