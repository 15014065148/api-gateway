package com.eveb.gateway.game.kg.model;

public class KgConstants {

    //以下需要在数据库做配置
    public static final String URL = "url"; //统一URL
    public static final String ENTERURL = "enterUrl"; //登陆/注册/玩游戏/大厅URL
    public static final String FIRSTTRANSFERURL = "firstTransferUrl"; //初步转账URL
    public static final String CONFIRMTRANSFERURL = "confirmTransferUrl"; //初步转账URL
    public static final String QUERYURL = "queryUrl"; //查询余额URL
    public static final String VENDORSITE = "vendorsite"; //网主 客户端前端网址
    public static final String FUNDLINK = "fundLink"; //网主给予玩家fund in/out 的网址
    public static final String VENDORID = "vendorId"; //( KenoGroup分配的 ) 网主ID
    public static final String PLAYERCURRENCY = "playerCurrency"; //玩家信用货币(KenoGroup会审核玩家之前的信用货币，不匹配将被驳回)
    public static final String PLAYERCREDIT = "playerCredit"; //默认为  0
    public static final String PLAYERALLOWSTAKE = "playerAllowStake"; //玩家选择赌注上限组别,（网主在KenoGroup管理网站设置的赌注组别） (测试环境 (TEST) 的的格式为xx,xx,xx ) (正式环境 (LIVE) 的格式为  1,2,3,4,5(任选) )
    public static final String TRIAL = "trial"; //试用版帐户 ( Yes -1 /No – 0 )
    public static final String LANGUAGE = "language"; //语言版本 ( 默认为  ‘ sc ’) ,(英文版本为'en')
    public static final String APIKEY = "apiKey"; // 默认为空; 呼叫API秘钥（可在后台设置秘钥）
    public static final String EDITPASSWORD = "editPassword"; //默认= 0 ； 1 = 跳转至设置手机密码页面 (注：gametype需放 0 才有效)
    public static final String REBATELEVEL = "rebateLevel"; //不同返水等級的返水百分比由網主設定,默认为1
    public static final String GAMETYPE = "gameType"; //不同返水等級的返水百分比由網主設定,默认为1
    public static final String PLAYERIP = "playerIP"; // 玩家 ip
    public static final String VENDORREF = "vendorRef"; //网主生成参考编号以供参考
    public static final String REMARKS = "remarks"; //备注信息

    //以下不需要在数据库做配置
    public static final String LINK = "Link"; //创建会员/会员登陆/玩游戏/打开大厅请求返回xml中的name节点值
    public static final String FUNDINTEGRATIONID = "FundIntegrationId"; //初步转账请求返回xml中的name节点值
    public static final String CREDIT = "Credit"; //确认转账请求返回xml中的name节点值
    public static final String REMAIN = "Remain"; //查询余额请求返回xml中的name节点值

    public enum ErrEnum {
        ENUM_ERR_1("VENDORNOTAUTHORIZED", "xmlrpc访问的IP与网主的设置不匹配 "),
        ENUM_ERR_2("VENDORACCOUNTSUSPENDED", "网主帐户暂停"),
        ENUM_ERR_3("VENDORTESTACCOUNTMODENOTALLOWREGISTERREALACCOUNT", "测试阶段不可创建正式账号"),
        ENUM_ERR_4("NOTACCEPTNEGATIVEAMOUNTTRANSFER", "转帐只接受正数或大于0"),
        ENUM_ERR_5("GAMESERVERMAINTENANCE", "游戏服务器关闭或无法连接"),
        ENUM_ERR_6("GAMESERVERNOTAPPROPRIATE", "连接游戏服务器的访问是不相关的网主"),
        ENUM_ERR_7("PLAYERACCOUNTSUSPENDED", "帐户已暂停"),
        ENUM_ERR_8("PLAYERCURRENCYNOTSUPPORTKenogroup", "系统不支援玩家所要求的货币"),
        ENUM_ERR_9("PLAYERCURRENCYNOTMATCHBEFORE", "玩家所要求的货币不匹配之前的货币记录"),
        ENUM_ERR_10("PLAYERSTAKENOTBELONGTOVENDOR(ONLYALLOW:[X,X,X])", "所要求的限红组不属于网主, 只允许( )内的限红组"),
        ENUM_ERR_11("ILLEGALCONTENT", "系统输入不支持五种符号(<)(>)(“)(@)(空格) "),
        ENUM_ERR_12("TRIALACCOUNTCONFLICT", "试用帐号冲突 测试环境 Trial = 1 正式环境 Trial = 0"),
        ENUM_ERR_13("YOUCHOOSEGAME,STAKELIMITNEEDONE", "游戏类型 只能支持一组限红"),
        ENUM_ERR_14("GAMETYPENOTBELONGTOVENDOR(ONLYALLOW:[0,2,4])", "游戏类型 只支持 【0=游戏大厅,2=时时彩,4=六合彩】"),
        ENUM_ERR_15("INCORRECTAPIKEYAPIKEY", "不匹配 （请检查后台设置的API KEY）"),
        ENUM_ERR_16("YOUHAVENOTOPENSSC", "SSC 游戏没开通"),
        ENUM_ERR_17("YOUHAVENOTOPENPK10", "PK10游戏没开通"),
        ENUM_ERR_18("YOUHAVENOTOPENHK49", "HK49游戏没开通"),
        ENUM_ERR_19("VENDORNOTAUTHORIZED", "xmlrpc的访问与网主设置不匹配"),
        ENUM_ERR_20("VENDORACCOUNTSUSPENDED", "网主帐户暂停"),
        ENUM_ERR_21("PLAYERNOTFOUND", "玩家账户不存在"),
        ENUM_ERR_22("NOTENOUGHCREDITTOFUNDOUT", "余额不足够 (提出)"),
        ENUM_ERR_23("PENDINGBETNOTALLOWTRANSFER", "因目前赌注中, 因此不允许的转账，除非赌注是超过20分钟。因为，澳大利亚和斯洛伐克允许下一天赌注。"),
        ENUM_ERR_24("FUNDINTEGRATIONIDNOTVALID", "余额ID已更新，不允许重复的更新"),
        ENUM_ERR_25("ILLEGALCONTENT", "系统输入不支持五种符号(<)(>)(“)(@)(空格) "),
        ENUM_ERR_26("FUNDINTEGRATIONIDNOTFOUND", "余额ID, 玩家ID, 网主ID,金额, 其中有不匹配"),
        ENUM_ERR_27("INCORRECTAPIKEYAPIKEY", "不匹配 （请检查后台设置的API KEY）"),
        ENUM_ERR_28("VENDORNOTAUTHORIZED", "xmlrpc的访问与网主设置不匹配"),
        ENUM_ERR_29("VENDORACCOUNTSUSPENDED", "网主帐户暂停"),
        ENUM_ERR_30("ILLEGALCONTENT", "系统输入不支持五种符号(<)(>)(“)(@)(空格) "),
        ENUM_ERR_31("PLAYERNOTFOUND", "玩家账户不存在"),
        ENUM_ERR_32("INCORRECTAPIKEYAPIKEY", "不匹配 （请检查后台设置的API KEY）"),
        ENUM_ERR_999("999", "失败");


        private String code;
        private String msg;

        ErrEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static String getMsg(String ecode) {
            for (KgConstants.ErrEnum e : KgConstants.ErrEnum.values()) {
                if (e.code.equals(ecode)) {
                    return e.msg;
                }
            }
            return ENUM_ERR_999.msg;
        }
    }

}

