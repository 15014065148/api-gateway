package com.eveb.gateway.game.pg.model;

import com.eveb.gateway.utils.MD5;
import lombok.Data;

/**
 * @Author: Miracle
 * @Description: 盘古彩播请求实体
 * @Date: 16:16 2018/8/1
 **/
public class PgParameterModel {

    @Data
    public static class RegisterModel {
        private String username;
        private String password;
        /**长度10位随机数字**/
        private String orderNumber;
        private String partner;
        private String sign;
        private String token;

        public String getToken() {
            return MD5.getMD5("partner=" + partner + "&orderNumber=" + orderNumber + "&username=" + username + "&password=" + password + "&sign=" + sign);
        }
    }


    @Data
    public static class LoginModel extends RegisterModel {
        private String login_side;
        private String ip;

        @Override
        public String getToken() {
            return MD5.getMD5("partner=" + this.getPartner() + "&orderNumber=" + this.getOrderNumber() + "&username=" + this.getUsername() + "&password=" + this.getPassword() + "&sign=" + this.getSign());
        }
    }

    @Data
    public static class BalanceModel extends RegisterModel {
        @Override
        public String getToken() {
            return MD5.getMD5("partner=" + this.getPartner() + "&orderNumber=" + this.getOrderNumber() + "&username=" + this.getUsername() + "&sign=" + this.getSign());
        }
    }

    @Data
    public static class TransferModel extends RegisterModel {
        private String type;
        private String amount;
        private String transationNo;

        @Override
        public String getToken() {
            return MD5.getMD5("partner=" + this.getPartner() + "&orderNumber=" + this.getOrderNumber() + "&username=" + this.getUsername() + "&type=" + type + "&amount=" + amount + "&transationNo=" + transationNo + "&sign=" + this.getSign());
        }
    }

    @Data
    public static class TransferCheckModel extends TransferModel {
        @Override
        public String getToken() {
            return MD5.getMD5("partner=" + this.getPartner() + "&orderNumber=" + this.getOrderNumber() + "&transationNo=" + this.getTransationNo() + "&sign=" + this.getSign());
        }
    }

}
