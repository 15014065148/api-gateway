package com.eveb.gateway.game.cq9.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BetRqModel {

    private String account;
    private String eventTime;
    private String gamehall;
    private String gamecode;
    private String roundid;
    private BigDecimal bet;
    private BigDecimal win;
    private BigDecimal amount;
    private String mtcode;

}
