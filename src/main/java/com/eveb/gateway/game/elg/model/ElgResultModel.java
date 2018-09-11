package com.eveb.gateway.game.elg.model;

import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.unity.model.UnityResultModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ElgResultModel {
    public UnityResultModel getCreatePlayerResult(String result) {
        if (result.trim().equals("1")) {
            return new UnityResultModel(true, ApplicationConstants.SUCCESS);
        }
        return new UnityResultModel(false, ElgConstants.ErrEnum.getMsg(result.split(",")[0].trim()));
    }

    public UnityResultModel getTransferResult(String result) {
        return getUnityResultModel(result);
    }

    public UnityResultModel getQueryBalanceResult(String result) {
        return getUnityResultModel(result);
    }

    public UnityResultModel getPlayGameResult(String result) {
        String[] resultArrays = result.split(",");
        if (resultArrays[0].trim().equals("1")) {
            return new UnityResultModel(true, resultArrays[1].trim());
        }
        return new UnityResultModel(false, ElgConstants.ErrEnum.getMsg(resultArrays[0].trim()));
    }

    private UnityResultModel getUnityResultModel(String result) {
        String[] resultArrays = result.split(",");
        if (resultArrays[0].trim().equals("1")) {
            return new UnityResultModel(true, new BigDecimal(resultArrays[1].trim()));
        }
        return new UnityResultModel(false, ElgConstants.ErrEnum.getMsg(resultArrays[0].trim()));
    }

}
