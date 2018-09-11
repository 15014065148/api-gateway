package com.eveb.gateway.game.cq9.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EndRoundRqModel {

    private String account;
    private String gamehall;
    private String gamecode;
    private String roundid;
    private transient List<Data> data;

    @lombok.Data
    public static class Data {
        private BigDecimal amount;
        private String mtcode;
        private String eventTime;
    }
}
