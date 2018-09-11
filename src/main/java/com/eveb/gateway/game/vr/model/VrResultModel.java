package com.eveb.gateway.game.vr.model;

import com.eveb.gateway.game.unity.model.UnityResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
public class VrResultModel {
    /**
     * 新增用户/注销用户/查询余额
     */
    public UnityResultModel getCommonResult(Map<String, Object> map) {
        if (map != null && map.containsKey("errorCode")) {
            if ((Integer) map.get("errorCode") == 0) {
                return new UnityResultModel(true, VrConstants.ErrEnum.getMsg((Integer) map.get("errorCode")));
            } else {
                try {
                    return new UnityResultModel(false, VrConstants.ErrEnum.getMsg((Integer) map.get("errorCode")));
                } catch (Exception e) {
                    return new UnityResultModel(false, VrConstants.ErrEnum.getMsg(999));
                }
            }
        } else if (map != null && map.containsKey("balance")) {
            if (!map.get("balance").toString().equals("-1.0")) {
                return new UnityResultModel(true, map.get("balance"));
            } else {
                try {
                    return new UnityResultModel(false, VrConstants.ErrEnum.getMsg(Integer.parseInt(map.get("balance").toString().substring(0,map.get("balance").toString().indexOf(".")))));
                } catch (Exception e) {
                    return new UnityResultModel(false, VrConstants.ErrEnum.getMsg(999));
                }
            }
        }
        return new UnityResultModel(false, VrConstants.ErrEnum.getMsg(999));
    }

    /**
     * 用户登陆
     */
    public UnityResultModel getLoginResult(String jumpUrl) {
        if (!StringUtils.isEmpty(jumpUrl)) {
            return new UnityResultModel(true, jumpUrl);
        } else {
            return new UnityResultModel(false, VrConstants.ErrEnum.getMsg(999));
        }
    }

    /**
     * 用户转账（存款/取款）
     */
    public UnityResultModel getTransferResult(Map<String, Object> map) {
        if (map.get("state") != null && (Integer) map.get("state") == 0) {
            return new UnityResultModel(true, VrConstants.ErrEnum.getMsg((Integer) map.get("state")));
        } else {
            try {
                return new UnityResultModel(false, VrConstants.ErrEnum.getMsg((Integer) map.get("state")));
            } catch (Exception e) {
                return new UnityResultModel(false, VrConstants.ErrEnum.getMsg(999));
            }
        }
    }

    /**
     * 用户确认转账
     */
    public UnityResultModel getcheckTransferResult(CheckTransferResultModel checkTransferResultModel) {
        log.info("VRRRRRRRRRRRRRRRRRRRR444444");
        if (checkTransferResultModel.getRecords() != null && checkTransferResultModel.getRecords().size() != 0 && checkTransferResultModel.getRecords().get(0).getState() == 0) {
            return new UnityResultModel(true, VrConstants.ErrEnum.getMsg(checkTransferResultModel.getRecords().get(0).getState()));
        } else {
            try {
                return new UnityResultModel(false, VrConstants.ErrEnum.getMsg(checkTransferResultModel.getRecords().get(0).getState()));
            } catch (Exception e) {
                return new UnityResultModel(false, VrConstants.ErrEnum.getMsg(999));
            }

        }
    }
}
