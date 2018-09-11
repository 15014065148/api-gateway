package com.eveb.gateway.game.gns.model;

public class GnsConstants {

    /**金額移轉**/
    public static final String URL_TRANSFER="m4/wallet/transfer";
    /**金額移轉 以分**/
    public static final int AMOUNT_UNIT=100;
    /**取得目前合作平台的用戶錢包**/
    public static final String URL_BANALCE="m4/wallet/balance/";
    /**依交易序號取得存取交易歷程查詢**/
    public static final String URL_QUERYTRANSFER="m4/wallet_log/query/entry/";
    /**依用戶id取得交易存取記錄 2016-01-15T00:06:25.166Z**/
    public static final String URL_QUERYOPERATE="m4/wallet_log/query/user/userId?startDate=%s&endDate=%s";
    public static final String URL_PLAYGAME_TRY="?partner=%s&session=%s&mode=play&language=zh-hans";
    public static final String URL_PLAYGAME="?partner=%s&session=%s&mode=real&language=zh-hans";

    public static final String URL_PREFIX_HTTP="http://";
    public static final String URL_PREFIX_HTTPS="https://";

    public static final String CURRENCY="CNY";

    public static final String HEAD_PARTNER="X-Genesis-PartnerToken";
    public static final String HEAD_GENESIS="X-Genesis-Secret";

    public interface Transfer{
        String TF_DEPOSIT="Deposit";
        String TF_WITHDRAW="Withdraw";
    }

    public interface JsonKey {
        String PARTNER_ID = "partner";
        String SECRET = "secret";
        String KEY_SSESSION="session_token";
    }

}
