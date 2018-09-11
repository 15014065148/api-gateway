package com.eveb.gateway.game.avia.model;

public class AviaConstants {

    public static final String MAP_ACCEPT="Authorization";

    public interface MethodConstatns {
        String METHOD_REGISTER = "api/user/register";
        String METHOD_LOGIN = "api/user/login";
        String METHOD_BALANCE = "api/user/balance";
        String METHOD_TRANSFER = "api/user/transfer";
        String METHOD_TRANSFER_STATUS = "api/user/transferinfo";
        String METHOD_PLAYGAME = "launch_game/";
        String METHOD_TRY_PLAYGAME = "api/user/guest";
    }

    public interface TransferType{
        /**转入*/
        String IN="IN";
        /**转出*/
        String OUT="OUT";
    }

}
