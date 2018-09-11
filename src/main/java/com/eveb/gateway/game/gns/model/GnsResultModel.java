package com.eveb.gateway.game.gns.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GnsResultModel {

    @Data
    public static class Balance{
        private String transaction_id;
        private String currency;
        private BigDecimal credits_transferred;
        private BigDecimal internal_balance;
        private String action;
        private String custom_json;
        private String provider_id;
        private String session_token;
    }
}
