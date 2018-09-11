package com.eveb.gateway.game.ea.model;

import java.io.Serializable;

import lombok.Data;


@Data
public class TSEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    private String pcUrl;//PC端口接口地址1

    private String agyAcc;//代理号

    private String md5Key;//密钥

    private Integer status;
}