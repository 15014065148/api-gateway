package com.eveb.gateway.game.avia.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/2 13:28
 **/
@Data
public class AviaParameterModel {

    @Data
    public static class RegisterModel {
        private String UserName;
        private String password;
    }

    @Data
    public static class LoginModel {
        private String username;
    }

    @Data
    public static class TransferModel {
        private String UserName;
        private Integer Money;
        private String Type;
        private String ID;
    }

}
