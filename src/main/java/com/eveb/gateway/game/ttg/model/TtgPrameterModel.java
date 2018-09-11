package com.eveb.gateway.game.ttg.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/29 17:49
 **/
@Data
public class TtgPrameterModel {

    private String uid;

    @Data
    public static class LoginModel{
        @Override
        public String toString(){
            /**默认采用中文和人民币**/
            return "<logindetail> <player account=\"CNY\" country=\"CN\" /> </logindetail>";
        }
    }

    @Data
    public static class LogoutModel{
        @Override
        public String toString(){
            /**默认采用中文和人民币**/
            return "<logindetail> <player account=\"CNY\" country=\"CN\" /> </logindetail>";
        }
    }

    @Data
    public static class TransferModel extends TtgPrameterModel{
        private BigDecimal amount;
        @Override
        public String toString(){
            /**默认采用中文和人民币**/
            return "<transactiondetail uid=\""+getUid()+"\" amount=\""+amount+"\" />";
        }
    }
}
