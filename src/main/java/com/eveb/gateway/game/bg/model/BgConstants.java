package com.eveb.gateway.game.bg.model;

import com.eveb.gateway.game.vr.model.VrConstants;

public class BgConstants {
    //在数据库进行配置
    public static final String GREATEUSER_METHOD = "createUserMethod";//新增用户
    public static final String USERLOGOUT_METHOD = "userLogOutMethod";//新增用户方法标识
    public static final String USERTRANSFER_METHOD = "userTransferMethod";//用户转账方法标识
    public static final String USEBALANCE_METHOD = "userBalanceMethod";//查询用户余额方法标识
    public static final String CHECKTRANSFER_METHOD = "checkTransferMethod";//确认转账方法标识
    public static final String AGENTPASSWORD = "agentPassword";//代理账号密码
    public static final String AGENTLOGINID = "agentLoginId";//代理账号
    public static final String JSONPC = "jsonPc";
    public static final String SN = "sn";
    public static final String SECRETKEY = "secretKey";
    public static final String LOCALE = "locale";
    public static final String VIDEOGAME_METHOD = "videoGameMethod";//视讯游戏方法标识 open.video.game.url
    public static final String FISHINGGAME_METHOD = "fishingGameMethod";//捕鱼游戏方法标识 open.game.bg.fishing.url
    public static final String EGAMEGAME_METHOD = "egameGameMethod";//电子游戏方法标识 open.game.bg.egame.url
    public static final String TRYVIDEOGAME_METHOD = "tryVideoGameMethod";//试玩视讯游戏方法标识 open.video.trial.game.url
    public static final String TRYFISHINGGAME_METHOD = "tryFishingGameMethod";//试玩捕鱼游戏方法标识 open.game.bg.fishing.trial.url
    public static final String TRYEGAMEGAME_METHOD = "tryEgameGameMethod";//试玩电子游戏方法标识 open.game.bg.egame.trial.url
    //不用在数据库进行配置

    public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public interface ResultConstants {
        String RESULT = "result";
        String ERROR = "error";
        String MESSAGE = "message";
        String TOTAL = "total";

    }

    public interface RequestParamMapConstants {
        String URL = "url";
        String ID = "id";
        String METHOD = "method";
        String JSONRPC = "jsonrpc";
        String RANDOM = "random";
        String SN = "sn";
        String LOGINID = "loginId";
        String SIGN = "sign";
        String DIGEST = "digest";
        String LOCALE = "locale";
        String ISMOBILEURL = "isMobileUrl";
        String ISHTTPSURL = "isHttpsUrl";
        String AMOUNT = "amount";
        String BIZID = "bizId";
        String STARTTIME = "startTime";
        String ENDTIME = "endTime";
        String PAGEINDEX = "pageIndex";
        String PAGESIZE = "pageSize";
        String NICKNAME = "nickname";
        String AGENTLOGINID = "agentLoginId";
    }

}
