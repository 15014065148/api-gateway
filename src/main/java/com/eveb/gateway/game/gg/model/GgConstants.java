package com.eveb.gateway.game.gg.model;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/6 11:37
 **/
public class GgConstants {


    /**打开游戏**/
    public static final String PARA_PLAYGAME ="cagent=%s/\\\\/loginname=%s/\\\\/password=%s/\\\\/method=fw/\\\\/sid=%s/\\\\/lang=%s/\\\\/gametype=%s/\\\\/ip=%s/\\\\/ishttps=%s";
    /**检测并创建游戏账号**/
    public static final String PARA_CHECKORCREATEGAMEACCOUT ="cagent=%s/\\\\/loginname=%s/\\\\/password=%s/\\\\/method=ca/\\\\/actype=1/\\\\/cur=%s";
    public static final String PARA_LOGOUT ="cagent=%s/\\\\/loginname=%s/\\\\/password=%s/\\\\/method=ty";
    /**转账**/
    public static final String PARA_TRANSFER="cagent=%s/\\\\/loginname=%s/\\\\/password=%s/\\\\/method=tc/\\\\/billno=%s/\\\\/type=%s/\\\\/credit=%s/\\\\/cur=%s";
    /**转账确认**/
    public static final String PARA_TRANSFERCREDITCONFIRM="cagent=%s/\\\\/loginname=%s/\\\\/method=tcc/\\\\/billno=%s/\\\\/type=%s/\\\\/credit=%s/\\\\/actype=1/\\\\/flag=%s/\\\\/password=123456/\\\\/cur=%s";
    /**余额查找**/
    public static final String PARA_GETBALANCE="cagent=%s/\\\\/loginname=%s/\\\\/password=%s/\\\\/method=gb/\\\\/actype=1/\\\\/cur=%s";
    /**状态查询**/
    public static final String PARA_QUERYORDERSTATUS="cagent=%s/\\\\/billno=%s/\\\\/method=qx";

    public static final String MAP_APIURL="apiUrl";
    public static final String MAP_CAGENT="cagent";
    public static final String MAP_DES_KEY="des_key";
    public static final String MAP_MD5_KEY="md5_key";

    public static final String TRANSFER_IN="IN";
    public static final String TRANSFER_OUT="OUT";

    public static final String OPEN_HALL_GAMETYPE="0";
}
