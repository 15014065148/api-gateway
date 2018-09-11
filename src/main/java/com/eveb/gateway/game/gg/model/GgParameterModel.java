package com.eveb.gateway.game.gg.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/2 13:28
 **/
@Data
public class GgParameterModel {

    private String cagent;
    private String loginname;
    private String password;
    private String cur;

    @Data
    public static class PlayGameModel extends GgParameterModel {
        private String sid;
        private String lang;
        private String gametype;
        private String ip;
        private String ishttp;
        @Override
        public String toString() {
            return String.format(GgConstants.PARA_PLAYGAME,this.getCagent(),this.getLoginname(),this.getPassword(),sid,lang,gametype,ip,ishttp);
        }
    }

    @Data
    public static class RegisterModel extends GgParameterModel {
        @Override
        public String toString() {
            return String.format(GgConstants.PARA_CHECKORCREATEGAMEACCOUT,this.getCagent(),this.getLoginname(),this.getPassword(),this.getCur());
        }
    }

    @Data
    public static class LogoutModel extends GgParameterModel {
        @Override
        public String toString() {
            return String.format(GgConstants.PARA_LOGOUT,this.getCagent(),this.getLoginname(),this.getPassword());
        }
    }

    @Data
    public static class BalanceModel extends GgParameterModel {
        @Override
        public String toString() {
            return String.format(GgConstants.PARA_GETBALANCE,this.getCagent(),this.getLoginname(),this.getPassword(),this.getCur());
        }
    }

    @Data
    public static class TransferModel extends GgParameterModel{
        private String billno;
        private String type;
        private BigDecimal credit;
        @Override
        public String toString() {
            return String.format(GgConstants.PARA_TRANSFER,this.getCagent(),this.getLoginname(),this.getPassword(),billno,type,credit,this.getCur());
        }
    }

    @Data
    public static class TransferStatusModel extends TransferModel {
        @Override
        public String toString() {
            return String.format(GgConstants.PARA_QUERYORDERSTATUS,this.getCagent(),this.getBillno());
        }
    }

    @Data
    public static class GameModel {
        private String member_code;
        private String game_code;
        private String game_type;
        private String language;
        private String ip;
        private String return_url;
    }

}
