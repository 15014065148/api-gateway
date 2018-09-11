package com.eveb.gateway.game.ttg.model;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/30 10:02
 **/
public class TtgConstants {


    public interface MethodConstants{
        String LOGIN="%scip/gametoken/%s";
        String LOGOUT="%scip/gametoken/";
        String BALANCE="%scip/player/%s/balance";
        String TRANSFER="%scip/transaction/%s/%s";
        String CHECKTRANSFER="%scip/transaction/%s/%s";
        String PLAYGAME="?playerHandle=%s&gameName=%s&gameId=%s&%s&gameSuite=%s&lang=zh-cn&account=CNY";
        String TRYPLAYGAME="?playerHandle=999999&account=FunAcct&gameName=%s&gameId=%s&%s&lang=zh-cn&lsdId=zero";
    }
}
