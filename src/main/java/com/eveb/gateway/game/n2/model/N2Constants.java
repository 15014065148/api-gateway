package com.eveb.gateway.game.n2.model;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/9/4 11:34
 **/
public class N2Constants {
    /**CNY 中国 人民币*/
   public static final String CNY="156";
    /**zh-CN 简体中文*/
   public static final String LAN="zh-CN";

    public interface CheckLoginConstans{
        String STATUS_SUCCESS="Success";
        String STATUS_FAIL="Fail";
        /**SUCCESS（成功）*/
        String STATUSCODE_0="0";
        /**ERR_INVALID_REQ（错误：无效请求）*/
        String STATUSCODE_001="001";
    }

    public interface MethodConstants{
        String LOGIN="SingleLogin?merchantcode=%s&lang=%s&userId=%s&uuId=%s";
        String DEPOSIT="transaction/PlayerDeposit";
        String ACTION_DEPOSIT="cdeposit";
        String WITHDRAWAL="transaction/PlayerWithdrawal";
        String ACTION_WITHDRAWAL="cwithdrawal";
        String BALANCE="transaction/CheckClient";
        String PLAYGAME_PC="SingleLogin?merchantcode=%s&lang=%s&userId=%s&uuId=%s&redirectURL=%s&gm=%s";
        String PLAYGAME_APP="azuriteapp://View?merchantcode=%s&lang=%s&userId=%s&uuId=%s&redirectURL=%s&gm=%s";
    }

}
