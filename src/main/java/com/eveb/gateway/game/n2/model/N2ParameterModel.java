package com.eveb.gateway.game.n2.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/29 10:37
 **/
@Data
public class N2ParameterModel {

    private String id;
    private String userid;
    private String vendorid;
    private String merchantpasscode;
    private String currencyid;

    @Data
    public static class LoginModel extends N2ParameterModel{
        private String password;

        @Override
        public String toString(){
            StringBuffer ps = new StringBuffer();
            ps.append(String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?> <request action=\"clogin\">"));
            ps.append(String.format("<element id=\""+this.getId()+"\">\n" +
                    "    <properties name=\"userid\">"+this.getUserid()+"</properties>\n" +
                    "    <properties name=\"password\">"+password+"</properties>\n" +
                    "  </element>"));
            ps.append("</request>");
            return ps.toString();
        }
    }

    @Data
    public static class BalanceModel extends N2ParameterModel{
        @Override
        public String toString(){
            return "<?xml version=\"1.0\" encoding=\"utf-16\"?>\n" +
                    "<request action=\"ccheckclient\">\n" +
                    "<element id=\""+this.getId()+"\">\n" +
                    "<properties name=\"userid\">"+this.getUserid()+"</properties>\n" +
                    "<properties name=\"vendorid\">"+this.getVendorid()+"</properties>\n" +
                    "<properties name=\"merchantpasscode\">"+this.getMerchantpasscode()+"</properties>\n" +
                    "<properties name=\"currencyid\">"+this.getCurrencyid()+"</properties>\n" +
                    "</element>\n" +
                    "</request>";
        }
    }

    @Data
    public static class TransferModel extends N2ParameterModel{
        private String action;
        private BigDecimal amount;
        private String refno;

        @Override
        public String toString() {
            StringBuffer ps = new StringBuffer();
            ps.append(String.format("<?xml version=\"1.0\" encoding=\"utf-16\"?> <request action=\"" + action + "\">"));
            ps.append(String.format("<element id=\"" + this.getId() + "\">\n" +
                    "<properties name=\"userid\">" + this.getUserid() + "</properties>\n"));
            if(N2Constants.MethodConstants.ACTION_DEPOSIT.equals(action)) {
                ps.append("<properties name=\"acode\"></properties>\n");
            }
            ps.append("<properties name=\"vendorid\">" + this.getVendorid() + "</properties>\n" +
                    "<properties name=\"merchantpasscode\">" + this.getMerchantpasscode() + "</properties>\n" +
                    "<properties name=\"amount\">" + amount + "</properties>\n" +
                    "<properties name=\"currencyid\">" + this.getCurrencyid() + "</properties>\n" +
                    "<properties name=\"refno\">" + refno + "</properties>\n" +
                    "</element>");
            ps.append("</request>");
            return ps.toString();
        }
    }



}
