package com.eveb.gateway.game.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Data
public class TGmApi implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer depotId;//平台代码

    private String depotName;

    private String apiName;//API接口名称

    /**API接口名称**/
    private String apiUrl;

    private String pcUrl;//PC端口接口地址1

    private String mbUrl;//PC端口接口地址1

    private String pcUrl2;//PC端口接口地址2

    private String agyAcc;//代理号

    private String md5Key;//密钥

    private String secureCode;//安全码格式(以JSON格式存储)

    private String proxyFore;//代理前缀代码

    private String prefix;

    private Byte available;//1开启，0禁用

    private String webName;//网站名称(BBIN)

    private Map<String, String> secureCodes;

    public Map<String, String> getSecureCodes() {
        try {
            secureCodes= JSON.parseObject(secureCode,Map.class);
            return secureCodes;
        }catch (Exception e){
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}