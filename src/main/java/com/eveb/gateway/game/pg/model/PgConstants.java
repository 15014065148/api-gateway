package com.eveb.gateway.game.pg.model;

/**
 * @Author: Miracle
 * @Description: 盘古彩播常量
 * @Date: 16:16 2018/8/1
 **/
public class PgConstants {

    /***status int 状态值 1：成功，0：失败***/
    public static String CODE_STATUS_SUC = "1";
    public static String CODE_STATUS_FAL = "0";

    public interface MethodConstatns {
        String METHOD_PLAYGAME="?token=";
        String METHOD_REGISTER = "register-user";
        String METHOD_LOGIN = "login-user";
        String METHOD_BALANCE = "balance-user";
        String METHOD_TRANSFER = "transfer-user";
        String METHOD_TRANSFER_STATUS = "transfer-status";
    }

    public interface OriginConstants{
        String PC="1";
        String H5="2";
        String APP="3";
    }

    public interface TransferConstants{
        String IN="in";
        String OUT="out";
    }

}
