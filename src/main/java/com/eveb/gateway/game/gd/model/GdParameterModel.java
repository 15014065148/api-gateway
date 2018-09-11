package com.eveb.gateway.game.gd.model;

import lombok.Data;

@Data
public class GdParameterModel {
    private String url;
    private String method;
    private String merchantID;
    private String messageID;
    private String userID;
    private String currencyCode;
    private String betGroup;
    private String affiliate;
    private String amount;
    private String enableInGameTransfer;
    private String getEndBalance;
    private String index;
    private String accessKey;
    private String lang;
    private String origin;
    private String createPlayerParamStr = "<?xml version=\"1.0\"?>\n" +
            "<Request> \n" +
            "  <Header> \n" +
            "    <Method>%s</Method>  \n" +
            "    <MerchantID>%s</MerchantID>  \n" +
            "    <MessageID>%s</MessageID> \n" +
            "  </Header>  \n" +
            "  <Param> \n" +
            "    <UserID>%s</UserID>  \n" +
            "    <CurrencyCode>%s</CurrencyCode>  \n" +
            "  </Param> \n" +
            "</Request>";
    private String logoutParamStr = "<?xml version=\"1.0\"?>\n" +
            "<Request> \n" +
            "  <Header> \n" +
            "    <Method>%s</Method>  \n" +
            "    <MerchantID>%s</MerchantID>  \n" +
            "    <MessageID>%s</MessageID> \n" +
            "  </Header>  \n" +
            "  <Param> \n" +
            "    <UserID>%s</UserID>  \n" +
            "  </Param> \n" +
            "</Request>";
    private String depositParamStr = "<?xml version=\"1.0\"?>\n" +
            "<Request>\n" +
            "  <Header>\n" +
            "    <Method>%s</Method>\n" +
            "    <MerchantID>%s</MerchantID>\n" +
            "    <MessageID>%s</MessageID>\n" +
            "  </Header>\n" +
            "  <Param>\n" +
            "    <UserID>%s</UserID>\n" +
            "    <CurrencyCode>%s</CurrencyCode>\n" +
            "    <Amount>%s</Amount>\n" +
            "  </Param>\n" +
            "</Request>";
    private String withdrawalParamStr = "<?xml version=\"1.0\"?>\n" +
            "<Request>\n" +
            "  <Header>\n" +
            "    <Method>%s</Method>\n" +
            "    <MerchantID>%s</MerchantID>\n" +
            "    <MessageID>%s</MessageID>\n" +
            "  </Header>\n" +
            "  <Param>\n" +
            "    <UserID>%s</UserID>\n" +
            "    <CurrencyCode>%s</CurrencyCode>\n" +
            "    <Amount>%s</Amount>\n" +
            "  </Param>\n" +
            "</Request>";
    private String queryBalanceParamStr = "<?xml version=\"1.0\"?>\n" +
            "<Request> \n" +
            "  <Header> \n" +
            "    <Method>%s</Method>  \n" +
            "    <MerchantID>%s</MerchantID>\n" +
            "    <MessageID>%s</MessageID> \n" +
            "  </Header>  \n" +
            "  <Param> \n" +
            "    <UserID>%s</UserID>\n" +
            "    <CurrencyCode>%s</CurrencyCode>\n" +
            "    <RequestBetLimit>1</RequestBetLimit> \n" +
            "  </Param> \n" +
            "</Request>";
    private String checkTransferParamStr = "<?xml version=\"1.0\"?>\n" +
            "<Request>\n" +
            "  <Header>\n" +
            "    <Method>%s</Method>\n" +
            "    <MerchantID>%s</MerchantID>\n" +
            "    <MessageID>%s</MessageID>\n" +
            "  </Header>\n" +
            "  <Param>\n" +
            "    <MessageID>%s</MessageID>\n" +
            "    <UserID>%s</UserID>\n" +
            "    <CurrencyCode>%s</CurrencyCode>\n" +
            "    </Param>\n" +
            "  </Request>";
    private String loginParamStr = "%s?LoginTokenID=%s&OperatorCode=%s&lang=%s&playerid=%s&Currency=%s&Key=%s&mobile=%s";
}
