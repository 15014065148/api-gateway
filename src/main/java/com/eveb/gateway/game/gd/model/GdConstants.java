package com.eveb.gateway.game.gd.model;

public class GdConstants {
    //以下需要在数据库中配置
    public static final String COMMONURL = "commonUrl";//http://wsgd.gdsecure88.com/MerchantAPI/ewallet.php
    public static final String LOGINURL = "loginUrl";//http://apiapi125.com/main.php
    public static final String CREATEMETHOD = "createMethod";//cCreateMember
    public static final String LOGOUTMETHOD = "logoutMethod";//cLogoutPlayer
    public static final String DEPOSITMETHOD = "depositMethod";//cDeposit
    public static final String WITHDRAWALMETHOD = "withdrawalMethod";//cWithdrawal
    public static final String CHECKTRANSFER = "checkTransfer";//cCheckTransactionStatus
    public static final String BALANCEMETHOD = "balanceMethod";//cCheckClient
    public static final String MERCHANTID = "merchantID";//EVEBtest
    public static final String CURRENCYCODE = "currencyCode";//CNY
    public static final String LANG = "lang";//zh-cn

    //以下不需要在数据库中配置
    public static final String CREATEMESSAGEIDPREFIX = "M";
    public static final String LOGOUTMESSAGEIDPREFIX = "L";
    public static final String CHECKCLIENTMESSAGEIDPREFIX = "S";
    public static final String BALANCEMESSAGEIDPREFIX = "C";
    public static final String DATEFORMAT = "yyMMddHHmmss";

    public enum ErrEnum {
        ENUM_ERR_0("0", "成功"),
        ENUM_ERR_201("201", "err_invalid_req"),
        ENUM_ERR_202("202", "err_db_operation"),
        ENUM_ERR_203("203", "err_exceed_amount"),
        ENUM_ERR_204("204", "err_locked_client"),
        ENUM_ERR_205("205", "err_client_in_game"),
        ENUM_ERR_206("206", "err_messageid_existed"),
        ENUM_ERR_207("207", "err_player_existed"),
        ENUM_ERR_999("999", "失败");


        private String code;
        private String msg;

        ErrEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static String getMsg(String ecode) {
            for (GdConstants.ErrEnum e : GdConstants.ErrEnum.values()) {
                if (e.code.equals(ecode)) {
                    return e.msg;
                }
            }
            return ENUM_ERR_999.msg;
        }
    }
}
