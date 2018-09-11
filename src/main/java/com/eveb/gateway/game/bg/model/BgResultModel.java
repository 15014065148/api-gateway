package com.eveb.gateway.game.bg.model;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.unity.model.UnityResultModel;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BgResultModel {

    public UnityResultModel getCreateUserResult(Map<String, Object> map) {
        return getResult(map, ApplicationConstants.SUCCESS, ApplicationConstants.FAIL);
    }

    public UnityResultModel getTransferResult(Map<String, Object> map) {
        return getResult(map, ApplicationConstants.SUCCESS, ApplicationConstants.FAIL);
    }

    public UnityResultModel getQueryBalanceResult(Map<String, Object> map) {
        return getResult(map, (map.get(BgConstants.ResultConstants.RESULT)!=null)?map.get(BgConstants.ResultConstants.RESULT).toString():ApplicationConstants.SUCCESS, ApplicationConstants.FAIL);
    }

    public UnityResultModel getcheckTransferResult(Map<String, Object> map) {
        if (map != null && map.get(BgConstants.ResultConstants.ERROR) == null && !JSON.parseObject(map.get(BgConstants.ResultConstants.RESULT).toString(), Map.class).get(BgConstants.ResultConstants.TOTAL).toString().equals("0")) {
            return new UnityResultModel(true, ApplicationConstants.SUCCESS);
        } else {
            try {
                return new UnityResultModel(false, JSON.parseObject(map.get(BgConstants.ResultConstants.ERROR).toString(), Map.class).get(BgConstants.ResultConstants.MESSAGE));
            } catch (Exception e) {
                return new UnityResultModel(false, ApplicationConstants.FAIL);
            }

        }
    }

    public UnityResultModel getPlayGameResult(Map<String, Object> map) {
        return getResult(map, (map.get(BgConstants.ResultConstants.RESULT)!=null)?map.get(BgConstants.ResultConstants.RESULT).toString():ApplicationConstants.SUCCESS, ApplicationConstants.FAIL);
    }

    public UnityResultModel getUserLogOutResult(Map<String, Object> map) {
        if (map != null && map.get(BgConstants.ResultConstants.RESULT).equals("1")) {
            return new UnityResultModel(true, ApplicationConstants.SUCCESS);
        } else {
            try {
                return new UnityResultModel(false, JSON.parseObject(map.get(BgConstants.ResultConstants.ERROR).toString(), Map.class).get(BgConstants.ResultConstants.MESSAGE));
            } catch (Exception e) {
                return new UnityResultModel(false, ApplicationConstants.FAIL);
            }

        }
    }

    private UnityResultModel getResult(Map<String, Object> map, String Success, String fail) {
        if (map != null && map.get(BgConstants.ResultConstants.ERROR) == null) {
            return new UnityResultModel(true, Success);
        } else {
            try {
                return new UnityResultModel(false, JSON.parseObject(map.get(BgConstants.ResultConstants.ERROR).toString(), Map.class).get(BgConstants.ResultConstants.MESSAGE));
            } catch (Exception e) {
                return new UnityResultModel(false, fail);
            }

        }
    }

}
