package com.eveb.gateway.game.unity.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferModel extends UnityParameterModel{


    private String orderNo;
    private BigDecimal amount;
}
