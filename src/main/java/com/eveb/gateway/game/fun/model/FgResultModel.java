package com.eveb.gateway.game.fun.model;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.game.unity.model.UnityResultModel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.Map;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/2 15:25
 **/
@Slf4j
public class FgResultModel {

    public static UnityResultModel postUnityResult(Response rs) {
        return checkRsCode(rs,FgConstants.ResultCodeConstants.SUC_CODE_201);
    }

    public static UnityResultModel getUnityResult(Response rs) {
        return checkRsCode(rs,FgConstants.ResultCodeConstants.SUC_CODE_200);
    }

    public static UnityResultModel delUnityResult(Response rs) {
        return checkRsCode(rs,FgConstants.ResultCodeConstants.SUC_CODE_204);
    }

    public static UnityResultModel playRsCode(Response rs) {
        String err = "";
        try {
            Map map = JSON.parseObject(rs.body().string());
            if (FgConstants.ResultCodeConstants.SUC_CODE_201 == rs.code()) {
                return new UnityResultModel(Boolean.TRUE, map.get(FgConstants.MAP_GAME_URL).toString()+"&"+FgConstants.MAP_GAME_TOKEN+"="+map.get(FgConstants.MAP_GAME_TOKEN));
            } else {
                err = FgConstants.ErrEnum.getMsg((int) map.get(FgConstants.MAP_ERR));
                return new UnityResultModel(Boolean.FALSE, err);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new UnityResultModel(Boolean.FALSE, err);
    }

    private static UnityResultModel checkRsCode(Response rs,int code) {
        String err = "";
        try {
            Map map = JSON.parseObject(rs.body().string());
            if (code == rs.code()) {
                String balance = "";
                if (map != null) {
                    balance = map.get(FgConstants.MAP_BALANCE) == null ? null : ((int)map.get(FgConstants.MAP_BALANCE)/100)+"";
                }
                return new UnityResultModel(Boolean.TRUE, balance);
            } else {
                err = FgConstants.ErrEnum.getMsg((int) map.get(FgConstants.MAP_ERR));
                return new UnityResultModel(Boolean.FALSE, err);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new UnityResultModel(Boolean.FALSE, err);
    }
}
