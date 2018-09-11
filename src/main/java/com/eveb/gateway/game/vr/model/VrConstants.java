package com.eveb.gateway.game.vr.model;

public class VrConstants {

    public static final String GREATEUSER_URL = "createUserUrl";//新增用户接口URL
    public static final String USERLOGIN_URL = "userLoginUrl";//用户登陆接口URL
    public static final String USERLOGOUT_URL = "userLogOutUrl";//用户注销接口URL
    public static final String USERDEPOSIT_URL = "userDepositUrl";//用户存款接口URL
    public static final String USERWITHDRAWAL_URL = "userWithdrawalUrl";//用户取款接口URL
    public static final String USEBALANCE_URL = "userBalanceUrl";//查询用户余额接口URL
    public static final String CHECKTRANSFER_URL = "checkTransferUrl";//确认转账接口URL


    public interface TransferConstants {
        Integer DEPOSIT_TYPE = 0;
        Integer WITHDRAWAL_TYPE = 1;
        Integer RECORDPAGE = 0;
        Integer RECORDCOUNTPERPAGE = 10;
    }

    public interface RequestConstants {
        String VR_ID = "id";
        String VR_VERSION = "version";
        String VR_DATA = "data";
    }

    public enum ErrEnum {
        ENUM_ERR_MINUS(-1, "此玩家不存在"),
        ENUM_ERR_0(0, "成功"),
        ENUM_ERR_1(1, "版本错误"),
        ENUM_ERR_2(2, "重试"),
        ENUM_ERR_3(3, "系统错误"),
        ENUM_ERR_4(4, "网络传送失败"),
        ENUM_ERR_5(5, "加密失败"),
        ENUM_ERR_6(6, "解密失败"),
        ENUM_ERR_7(7, "没有此商户"),
        ENUM_ERR_8(8, "没有此玩家"),
        ENUM_ERR_9(9, "新增玩家失败"),
        ENUM_ERR_10(10, "余额不足"),
        ENUM_ERR_11(11, "URL错误"),
        ENUM_ERR_12(12, "此玩家被禁用"),
        ENUM_ERR_13(13, "玩家钱包转点-种类错误"),
        ENUM_ERR_14(14, "玩家钱包转点-订单号重复"),
        ENUM_ERR_15(15, "玩家钱包转点-订单号内容错误"),
        ENUM_ERR_16(16, "玩家钱包转点-订单建立时间过太久"),
        ENUM_ERR_17(17, "玩家钱包转点-金额错误"),
        ENUM_ERR_18(18, "新增玩家失败，失败原因是此玩家已存在"),
        ENUM_ERR_999(999, "未知错误");

        private int code;
        private String msg;

        ErrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static String getMsg(int ecode) {
            for (VrConstants.ErrEnum e : VrConstants.ErrEnum.values()) {
                if (e.code == ecode) {
                    return e.msg;
                }
            }
            return ENUM_ERR_999.msg;
        }
    }
}
