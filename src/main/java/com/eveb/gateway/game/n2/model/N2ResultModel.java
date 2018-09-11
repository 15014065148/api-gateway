package com.eveb.gateway.game.n2.model;

import com.eveb.gateway.constants.ApplicationConstants;
import lombok.Data;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/9/3 18:08
 **/
public class N2ResultModel {

    @Data
    public static class CheckLoginModel{
        private String status;
        private String id;
        private String userid;
        private String uuid;
        private String vendorid;
        private String merchantpasscode;
        private String statusCode;

        @Override
        public String toString(){

            return "<?xml version=\"1.0\" encoding=\"utf-16\"?>\n" +
                    "<message>\n" +
                    "<status>"+status+"</status>\n" +//商家网站返回状态- Success （成功）- Fail （失败）
                    "<result action=\"userverf\">\n" +//action 节点必须是 userverf
                    "<element id=\""+id+"\">\n" +//N2Live 自动登入所生成的 ID 是用来处理特定的自动登入验证ID 需以大字母“L”开始并接着不超过 40 个数字
                    "<properties name=\"userid\">"+userid+"</properties>\n" +//玩家登入 ID （用于登入游戏的用户名）
                    "<properties name=”username”>"+userid+"</properties>\n" +// 玩家昵称，长度支持最多 16 个字符。
                    "<properties name=”uuid”>"+uuid+"</properties>\n" +//由商家伺服器生成的唯一 ID 例如：会话 ID 或认证 ID
                    "<properties name=”vendorid”>"+vendorid+"</properties> \n" +//商家身份ID(将由N2Live提供)
                    "<properties name=\"merchantpasscode\">"+merchantpasscode+"</properties> \n" +//由N2Live提供给商家的验证密码
                    "<properties name=”clientip”>"+ApplicationConstants.DEFAULT_IP +"</properties> \n" +//玩家的 IP 地址
                    "<properties name=”currencyid”>"+N2Constants.CNY+"</properties>\n" +//货币ID（根据国际标准化组织的数字标准）例如：“156”，“840”等，见附录 2
                    "<properties name=\"acode\"></properties>\n" +//合营商代码最多支持50个字符 合营商代码区分大小写合营商代码将不支持所有以上2.1.1提到的特别符号
                    "<properties name=”errdesc”></properties> \n" +//错误信息非零答复
                    "<properties name=”status”>"+statusCode+"</properties>\n" +
                    "</element>\n" +
                    "</result>\n" +
                    "</message>\n";
        }
    }

    @Data
    public  static class TransferModel{
        private String id;
        private String acode;
        private String status;
        private String paymentid;
        private String refno;
        private String errdesc;
        private String vendorid;
        private String merchantpasscode;

        @Override
        public String toString(){
            return "<?xml version=\"1.0\" encoding=\"utf-16\"?>\n" +
                    "<request action=\"cdeposit-confirm\">\n" +
                    "<element id=\""+id+"\">\n" +
                    "<properties name=\"acode\"></properties>\n" +
                    "<properties name=\"status\">"+status+"</properties>\n" +
                    "<properties name=\"paymentid\">"+paymentid+"</properties>\n" +
                    "<properties name=\"vendorid\">"+vendorid+"</properties>\n" +
                    "<properties name=\"merchantpasscode\">"+merchantpasscode+"</properties>\n" +
                    "<properties name=\"errdesc\"></properties>\n" +
                    "</element>\n" +
                    "</request>";
        }
    }

    @Data
    public static class BalanceModel{
        private String id;
        private String status;
        private String balance;
        private String errdesc;
    }
}
