package com.eveb.gateway.game.pg.model;

import com.eveb.gateway.game.unity.model.UnityResultModel;
import lombok.Data;

/**
 * @Author: Miracle
 * @Description: 盘古彩播返回实体
 * @Date: 16:16 2018/8/1
 **/
@Data
public class PgResultModel {

    private String status;
    private String error;
    private String balance;
    private String token;


    public static UnityResultModel unityResult(PgResultModel rs) {
        if (PgConstants.CODE_STATUS_SUC.equals(rs.getStatus())) {
            return new UnityResultModel(Boolean.TRUE, rs.balance == null ? rs.token : rs.balance);
        } else {
            return new UnityResultModel(Boolean.FALSE, rs.error);
        }
    }

}
