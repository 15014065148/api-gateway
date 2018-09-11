package com.eveb.gateway.game.kg.model;

import lombok.Data;

@Data
public class KgParameterModel {
    private String url;
    private String enterUrl;
    private String firstTransferUrl;
    private String confirmTransferUrl;
    private String queryUrl;
    private String vendorsite;
    private String fundLink; //网主给予玩家fund in/out 的网址
    private String vendorId; //( KenoGroup分配的 ) 网主ID
    private String playerId; // 网主分配给玩家ID (独特的ID)
    private String playerRealName; //显示玩家名称
    private String playerCurrency; //玩家信用货币(KenoGroup会审核玩家之前的信用货币，不匹配将被驳回)
    private String playerCredit; //默认为  0
    private String playerAllowStake; //玩家选择赌注上限组别,（网主在KenoGroup管理网站设置的赌注组别） (测试环境 (TEST) 的的格式为xx,xx,xx ) (正式环境 (LIVE) 的格式为  1,2,3,4,5(任选) )
    private String trial; //试用版帐户 ( Yes -1 /No – 0 )
    private String gameType; //登录 游戏大厅=0（默认）, 时时彩=2 ,北京PK10=3,六合彩=4
    private String language; //语言版本 ( 默认为  ‘ sc ’) ,(英文版本为'en')
    private String editPassword; //默认= 0 ； 1 = 跳转至设置手机密码页面 (注：gametype需放 0 才有效)
    private String rebateLevel; //不同返水等級的返水百分比由網主設定,默认为1
    private String playerIP; // 玩家 ip
    private String vendorRef; //网主生成参考编号以供参考
    private String remarks; //备注信息
    private String amount ; //玩家转帐的数目, 正值 (+) 代表 存款进 KG, 负值 (-) 代表从KG提款.
    private String apiKey ; //默认为空; 呼叫API秘钥（可在后台设置秘钥）
    private String fundIntegrationId ; //把成功的XML回应（初步转帐）提交到 （确认转帐）以便审核
    private String commonRequestParam="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
            "<methodCall>\n" +
            "<methodName>PlayerLanding</methodName>\n" +
            "<params>\n" +
            "<param>\n" +
            "<value>\n" +
            "<struct>\n" +
            "<member><name>VendorSite</name><value><string>%s</string></value></member>\n" +
            "<member><name>FundLink</name><value><string>%s</string></value></member>\n" +
            "<member><name>VendorId</name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerId</name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerRealName</name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerCurrency</name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerCredit</name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerAllowStake</name><value><string>%s</string></value></member>\n" +
            "<member><name>Trial</name><value><string>%s</string></value></member>\n" +
            "<member><name>GameType</name><value><string>%s</string></value></member>\n" +
            "<member><name>Language</name><value><string>%s</string></value></member>\n" +
            "<member><name>APIKey</name><value><string>%s</string></value></member>\n" +
            "<member><name>EditPassword</name><value><string>%s</string></value></member>\n" +
            "<member><name> RebateLevel </name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerIP</name><value><string>%s</string></value></member>\n" +
            "<member><name>VendorRef</name><value><string>%s</string></value></member>\n" +
            "<member><name>Remarks</name><value><string>%s</string></value></member>\n" +
            "</struct>\n" +
            "</value>\n" +
            "</param>\n" +
            "</params>\n" +
            "</methodCall>\n";
    private String firstTransferRequestParam="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
            "<methodCall>\n" +
            "<methodName>FundInOutFirst</methodName>\n" +
            "<params>\n" +
            "<param>\n" +
            "<value>\n" +
            "<struct>\n" +
            "<member><name>VendorId</name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerId</name><value><string>%s</string></value></member>\n" +
            "<member><name>Amount</name><value><string>%s</string></value></member>\n" +
            "<member><name>APIKey</name><value><string>%s</string></value></member>\n" +
            "</struct>\n" +
            "</value>\n" +
            "</param>\n" +
            "</params>\n" +
            "</methodCall>\n";
    private String confirmTransferRequestParam="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
            "<methodCall>\n" +
            "<methodName>FundInOutConfirm</methodName>\n" +
            "<params>\n" +
            "<param>\n" +
            "<value>\n" +
            "<struct>\n" +
            "<member><name>VendorId</name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerId</name><value><string>%s</string></value></member>\n" +
            "<member><name>Amount</name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerIP</name><value><string>%s</string></value></member>\n" +
            "<member><name>APIKey</name><value><string>%s</string></value></member>\n" +
            "<member><name>FundIntegrationId</name><value><string>%s</string></value></member>\n" +
            "<member><name>VendorRef</name><value><string>%s</string></value></member>\n" +
            "</struct>\n" +
            "</value>\n" +
            "</param>\n" +
            "</params>\n" +
            "</methodCall>\n";
    private String queryBalanceRequestParam="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
            "<methodCall>\n" +
            "<methodName>GetCredit</methodName>\n" +
            "<params>\n" +
            "<param>\n" +
            "<value>\n" +
            "<struct>\n" +
            "<member><name>VendorId</name><value><string>%s</string></value></member>\n" +
            "<member><name>PlayerId</name><value><string>%s</string></value></member>\n" +
            "<member><name>APIKey</name><value><string>%s</string></value></member>\n" +
            "</struct>\n" +
            "</value>\n" +
            "</param>\n" +
            "</params>\n" +
            "</methodCall>\n";
}
