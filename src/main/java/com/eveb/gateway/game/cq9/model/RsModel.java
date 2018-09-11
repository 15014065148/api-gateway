package com.eveb.gateway.game.cq9.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RsModel {


    private Status status;
    private Data data;

    @lombok.Data
    public static class Status{
        private String code="200";
        private String message;
        private String datetime;

        public Status() {
        }
    }
    @lombok.Data
    public static class Data{
        private BigDecimal balance;
        private String currency;
    }
}
