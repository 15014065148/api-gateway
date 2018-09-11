package com.eveb.gateway.game.bbin.model;

import com.eveb.gateway.game.unity.model.LoginModel;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Data
public class BbinParameterModel {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    // 接口函数专用关键字
    //建立用户
    public static final String BBIN_API_CREATEMEMBER="CreateMember";
    //登陆
    public static final String BBIN_API_LOGIN="Login";
    //登出
    public static final String BBIN_API_LOGOUT="Logout";
    public static final String BBIN_API_CHECKUSRBALANCE="CheckUsrBalance";


    public static final String BBIN_API_TRANSFER="Transfer";
    public static final String BBIN_API_CHECKTRANSFER="CheckTransfer";
    public static final String BBIN_API_TRANSFERRECORD="TransferRecord";


    public static final String BBIN_API_BETRECORD="BetRecord";
    public static final String BBIN_API_BETRECORDBYMODIFIEDDATE3="BetRecordByModifiedDate3";
    public static final String BBIN_API_LOGIN2="Login2";

    public static final String BBIN_API_PLAYGAME = "PlayGame";
    public static final String BBIN_API_PLAYGAMEBYH5 = "PlayGameByH5";
    public static final String BBIN_API_GETJPHISTORY = "GetJPHistory";

    private String validDate;

    public static String getValidDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -12);
        return sdf.format(cal.getTime());
    }

    @Data
    public static class CreatePlayer {
        private String website;
        private String username;
        private String uppername;
        private String key;
    }

    @Data
    public static class PlayGame {
        private String website;
        private String username;
        private String gamekind;   //游戏类型
        private String gametype;   //游戏id
        private String gamecode;
        private String key;
    }

    @Data
    public static class Login extends LoginModel {
        private String website;
        private String username;
        private String uppername;
        private String password;
        private String key;
    }

    @Data
    public static class Logout extends LoginModel {
        private String website;
        private String username;
        private String key;
    }

    @Data
    public static class Transfer{
        //網站名稱
        private String website;
        private String username;
        private String uppername;
        //轉帳序號(唯一值)，可用貴公司轉帳紀錄的流水號，以避免重覆轉帳< 請用int(19)( 1~9223372036854775806)來做設定 >，別名transid
        private String remitno;
        private String action;
        //IN(轉入額度) OUT(轉出額度)
        private String key;
        private Integer remit;

        public static final String IN="IN";
        public static final String OUT="OUT";
    }

    @Data
    public static class CheckTransfer{
        //網站名稱
        private String website;
        //轉帳序號(唯一值)，可用貴公司轉帳紀錄的流水號，以避免重覆轉帳< 請用int(19)( 1~9223372036854775806)來做設定 >，別名transid
        private String transid;
        private String key;
    }

    @Data
    public static class Balance{
        private String website;
        private String username;
        private String uppername;
        private Integer page;
        private Integer pagelimit;
        private String key;
    }
}
