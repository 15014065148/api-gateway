package com.eveb.gateway.game.elg.model;

public class ElgConstants {

    //以下需要在数据库中进行配置
    public static final String KEY = "key";
    public static final String PWD = "pwd";
    public static final String SYSTEM = "system";
    public static final String CURRENCY = "currency";
    public static final String LANGUAGE = "language";
    public static final String IP = "ip";
    public static final String SERVER = "server";
    public static final String PASSWORD = "password";


    public enum ErrEnum {

        ENUM_ERR_11("11", "wrong authorization"),
        ENUM_ERR_12("12", "Wrong authorization ip"),
        ENUM_ERR_13("13", "Wrong action"),
        ENUM_ERR_14("14", "Wrong incoming params"),
        ENUM_ERR_15("15", "Wrong authorization hash"),
        ENUM_ERR_16("16", "Login exists"),
        ENUM_ERR_17("17", "User not found"),
        ENUM_ERR_18("18", "Wrong merchant access"),
        ENUM_ERR_19("19", "Transaction fail"),
        ENUM_ERR_20("20", "Merchant transaction fail"),
        ENUM_ERR_21("21", "Can't get balance"),
        ENUM_ERR_22("22", "Wrong user password"),
        ENUM_ERR_23("23", "User disabled"),
        ENUM_ERR_24("24", "Redirect error"),
        ENUM_ERR_25("25", "Key error"),
        ENUM_ERR_26("26", "User creation error"),
        ENUM_ERR_27("27", "Your stall disabled"),
        ENUM_ERR_28("28", "Your net disabled"),
        ENUM_ERR_29("29", "Your API disabled"),
        ENUM_ERR_30("30", "Transactions ID failed value"),
        ENUM_ERR_31("31", "Wrong currency"),
        ENUM_ERR_32("32", "Wrong start or end dates"),
        ENUM_ERR_33("33", "This site is on maintenance"),
        ENUM_ERR_34("34", "Invalid Request"),
        ENUM_ERR_999("999", "失败");


        private String code;
        private String msg;

        ErrEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static String getMsg(String ecode) {
            for (ElgConstants.ErrEnum e : ElgConstants.ErrEnum.values()) {
                if (e.code.equals(ecode)) {
                    return e.msg;
                }
            }
            return ENUM_ERR_999.msg;
        }
    }
}
