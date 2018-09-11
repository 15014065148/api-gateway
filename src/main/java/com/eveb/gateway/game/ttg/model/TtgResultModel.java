package com.eveb.gateway.game.ttg.model;

import com.eveb.gateway.game.unity.model.UnityResultModel;
import com.eveb.gateway.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/30 16:41
 **/
@Slf4j
public class TtgResultModel {

    public static UnityResultModel unityLoginResult(String rs) {
        Map map = XmlUtil.xmlToMap(rs);
        return new UnityResultModel(Boolean.TRUE, map.get("token"));
    }

    public static UnityResultModel unityBalanceResult(Response rs) {
        return unityResultModel(rs, "real");
    }

    public static UnityResultModel unityChkTransferResult(Response rs) {
        return unityResultModel(rs, "amount");
    }

    public static UnityResultModel unityTransferResult(Response rs) {
        return unityResultModel(rs, "retry");
    }

    private static UnityResultModel unityResultModel(Response rs, String key) {
        try {
            if (rs != null) {
                Map map = XmlUtil.xmlToMap(rs.body().string());
                if (rs.code() == 200) {
                    return new UnityResultModel(Boolean.TRUE, map.get(key));
                }
                String msg = map.get("message").toString();
                return new UnityResultModel(Boolean.FALSE, msg.substring(0, msg.indexOf("===")));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return new UnityResultModel(Boolean.FALSE, "");
    }
}
