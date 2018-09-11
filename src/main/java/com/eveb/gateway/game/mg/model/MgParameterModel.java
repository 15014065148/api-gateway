package com.eveb.gateway.game.mg.model;

import lombok.Data;

@Data
public class MgParameterModel {

    @Data
    public static class MgCreation {
        private String crId;
        private String crType;
        private String neId;
        private String neType;
        private String tarType;
        private String username;
        private String name;
        private String password;
        private String confirmPassword;
        private String currency;
        private String language;
        private String email;
        private String mobile;
        private Casino casino;
        private Poker poker;

        @Data
        public static class Casino {
            private boolean enable;

            public Casino(boolean enable) {
                this.enable = enable;
            }
        }

        @Data
        public static class Poker {
            private boolean enable;

            public Poker(boolean enable) {
                this.enable = enable;
            }
        }
    }

    @Data
    public class MgLaunchGm {
        private String timestamp;
        private String apiusername;
        private String apipassword;
        private String token;
        private String language;
        private String gameId;
        private String bankingUrl;
        private String lobbyUrl;
        private String logoutRedirectUrl;
        private String demoMode;
        private String titanium;

        @Override
        public String toString() {
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("<mbrapi-launchurl-call");
            sBuffer.append(" timestamp=\"" + timestamp + "\"");
            sBuffer.append(" apiusername=\"" + apiusername + "\"");
            sBuffer.append(" apipassword=\"" + apipassword + "\"");

            sBuffer.append(" token=\""+token+"\"");
            sBuffer.append(" language=\"" + language + "\"");
            sBuffer.append(" gameId=\"" + gameId + "\"");
            sBuffer.append(" bankingUrl=\"" + bankingUrl + "\"");
            sBuffer.append(" lobbyUrl=\"" + lobbyUrl + "\"");

            sBuffer.append(" logoutRedirectUrl=\"" + logoutRedirectUrl + "\"");
            sBuffer.append(" demoMode=\"" + demoMode + "\"");
            sBuffer.append(" titanium=\"" + titanium + "\"");
            sBuffer.append("/>");
            return sBuffer.toString();
        }
    }

    @Data
    public static class MgLogin {
        private String timestamp;
        private String apiusername;
        private String apipassword;
        private String username;
        private String password;
        private String ipaddress;
        private String partnerId;
        private String currencyCode;

        @Override
        public String toString() {
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("<mbrapi-login-call");
            sBuffer.append(" timestamp=\"" + timestamp + "\"");
            sBuffer.append(" apiusername=\"" + apiusername + "\"");
            sBuffer.append(" apipassword=\"" + apipassword + "\"");
            sBuffer.append(" username=\"" + username + "\"");
            sBuffer.append(" password=\"" + password + "\"");
            sBuffer.append(" ipaddress=\"" + ipaddress + "\"");
            sBuffer.append(" partnerId=\"" + partnerId + "\"");
            sBuffer.append(" currencyCode=\"" + currencyCode + "\"");
            sBuffer.append("/>");
            return sBuffer.toString();
        }
    }

    @Data
    public static class MgTransfer {
        private String timestamp;
        private String apiusername;
        private String apipassword;
        private String token;
        private String product;
        private String operation;
        private String amount;
        private String txId;

        @Override
        public String toString() {
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("<mbrapi-changecredit-call");
            sBuffer.append(" timestamp=\"" + timestamp + "\"");
            sBuffer.append(" apiusername=\"" + apiusername + "\"");
            sBuffer.append(" apipassword=\"" + apipassword + "\"");
            sBuffer.append(" token=\"" + token + "\"");
            sBuffer.append(" product=\"" + product + "\"");
            sBuffer.append(" operation=\"" + operation + "\"");
            sBuffer.append(" amount=\"" + amount + "\"");
            sBuffer.append(" tx-id=\"" + txId + "\"");
            sBuffer.append("/>");
            return sBuffer.toString();
        }
    }

    @Data
    public static class Balance {
        private String timestamp;
        private String apiusername;
        private String apipassword;
        private String token;

        @Override
        public String toString() {
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append("<mbrapi-account-call");
            sBuffer.append(" timestamp=\"" + timestamp + "\"");
            sBuffer.append(" apiusername=\"" + apiusername + "\"");
            sBuffer.append(" apipassword=\"" + apipassword + "\"");
            sBuffer.append(" token=\"" + token + "\"");
            sBuffer.append("/>");
            return sBuffer.toString();
        }
    }

}
