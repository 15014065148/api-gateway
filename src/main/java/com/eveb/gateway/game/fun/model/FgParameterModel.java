package com.eveb.gateway.game.fun.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/2 13:28
 **/
@Data
public class FgParameterModel {

    @Data
    public static class RegisterModel {
        private String member_code;
        private String password;
    }

    @Data
    public static class TransferModel {
        private String member_code;
        private Integer amount;
        private String externaltransactionid;
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
