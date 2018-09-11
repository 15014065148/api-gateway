package com.eveb.gateway.constants;

public class ApplicationConstants {

    /**
     * REDIS 缓存 keys
     */
    public static final String REDIS_GAME_API_CACHE_KEY = "GameApiCache";

    public static final String REIDS_DEPOT_TOKEN_KEY = "depotTokenCache";

    public static final String REIDS_SITE_API_KEY = "SiteApiCache";

    public static final String REIDS_SESSION_KEY = "CallBackSessionCache";

    /**
     * 默认对于未有密码需求的第三方平台默认设置为123456
     ***/
    public static final String DEFAULT_USER_PASSWORD = "123456";
    public static final String DEFAULT_IP = "127.0.0.1";
    public static final String DEFAULT_CURRENCY = "CNY";
    public static final String DEFAULT_LANG = "zh-CN";

    //项目名称
    public static final String PROJECT_NAME = "Api-Gateway";

    //游戏类型
    public interface GameTypeConstants {
        String LIVE = "live";//真人
        String HUNTER = "hunter";//捕鱼
        String SLOT = "slot";//电子
        String LOTTERY = "lottery";//彩票
        String SPORTS = "sports";//体育
    }

    //操作结果
    public static final String SUCCESS = "操作成功";
    public static final String FAIL = "操作失败";
    public static final String ACTION_ERROR = "线路管控异常";

    //游戏客户端
    public static final String ORIGIN_PC = "PC";
    public static final String ORIGIN_APP = "APP";
    public static final String ORIGIN_H5 = "H5";
}
