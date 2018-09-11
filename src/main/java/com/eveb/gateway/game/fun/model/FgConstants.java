package com.eveb.gateway.game.fun.model;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/1 17:54
 **/
public class FgConstants {

    public static final String MAP_ACCEPT="Accept";
    public static final String MAP_ACCEPT_VALUE="application/json";
    public static final String MAP_MHNAME="merchantname";
    public static final String MAP_MHCODE="merchantcode";
    public static final String MAP_ERR="error_code";
    public static final String MAP_BALANCE="balance";

    public static final String MAP_GAME_URL="game_url";
    public static final String MAP_GAME_TOKEN="token";

    public static final String DEFAULT_VALUE_GAMETYPE="h5";
    public static final String DEFAULT_VALUE_LANGUAGE="zh-cn";
    public static final String DEFAULT_VALUE_RETURNURL="http://baidu.com";

    /**金額移轉 以分**/
    public static final int AMOUNT_UNIT=100;

    public interface ResultCodeConstants{
        int SUC_CODE_201=201;
        int SUC_CODE_200=200;
        /**登出成功使用*/
        int SUC_CODE_204=204;
        int ERR_CODE_400=400;

    }

    public interface MethodConstatns {
        String METHOD_REGISTER = "players";
        String METHOD_OPEN_HALL = "app/get_token_qr/member_code/";
        String METHOD_BALANCE = "player_chips/member_code/";
        String METHOD_TRANSFER = "player_uchips/member_code/";
        String METHOD_TRANSFER_STATUS = "player_uchips_check/";
        String METHOD_PLAYGAME = "launch_game/";
        /**登出**/
        String METHOD_LOGOUT = "player_sessions/member_code/";
    }

    public enum ErrEnum{
        ENUM_ERR_0(0,"内部错误"),
        ENUM_ERR_1(1,"错误请求,MERCHANTNAME"),
        ENUM_ERR_2(2,"重试"),
        ENUM_ERR_4(4,"代理商余额不足"),
        ENUM_ERR_50(50,"未授权,不能查询其他运营商信息"),
        ENUM_ERR_51(51,"未授权,不能查询其他运营商信息"),
        ENUM_ERR_52(52,"IP地址被阻止"),
        ENUM_ERR_53(53,"该玩家不存在"),
        ENUM_ERR_54(54,"玩家已经存在"),
        ENUM_ERR_56(56,"该游戏不存在"),
        ENUM_ERR_57(57,"账户被冻结"),
        ENUM_ERR_100(100,"必填字段不能为空"),
        ENUM_ERR_101(101,"玩家代码必须是 5-32 个字符，并没有特殊的字符之间"),
        ENUM_ERR_103(103,"无效的产品类型"),
        ENUM_ERR_105(105,"无效的外部交易"),
        ENUM_ERR_106(106,"时间格式非法"),
        ENUM_ERR_107(107,"游戏类型参数非法"),
        ENUM_ERR_108(108,"Illegal membercode parameters"),
        ENUM_ERR_109(109,"筹码值非法"),
        ENUM_ERR_110(110,"参数非法"),
        ENUM_ERR_111(111,"该玩家不在线或不存在"),
        ENUM_ERR_404(404,"玩家余额不足"),
        ENUM_ERR_405(405,"筹码更新失败"),
        ENUM_ERR_500(500,"无效的游戏代码"),
        ENUM_ERR_501(501,"该游戏不在代理商选中游戏列中禁止访问"),
        ENUM_ERR_550(550,"无效的密码值"),
        ENUM_ERR_1001(1001,"采集数据超时"),
        ENUM_ERR_1501(1501,"无代理商大厅url"),
        ENUM_ERR_1502(1502,"无效的游戏类型"),
        ENUM_ERR_1503(1503,"IP被阻止"),
        ENUM_ERR_1504(1504,"代理商请求过多被阻止"),
        ENUM_ERR_1505(1505,"该请求的url不存在"),
        ENUM_ERR_1506(1506,"由管理员踢出游戏，暂时无法登录游戏"),
        ENUM_ERR_1507(1507,"玩家登录状态异常"),
        ENUM_ERR_1513(1513,"玩家正在结算无法提现"),
        ENUM_ERR_1514(1514,"正在赌注中無法切換遊戲"),
        ENUM_ERR_1515(1515,"单号不存在或者该注单失败"),
        ENUM_ERR_1600(1600,"api维护中");

        private int code;
        private String msg;

        ErrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static String getMsg(int ecode) {
            for(ErrEnum e:ErrEnum.values()){
                if(e.code==ecode){
                    return e.msg;
                }
            }
            return ENUM_ERR_0.msg;
        }
    }
}
