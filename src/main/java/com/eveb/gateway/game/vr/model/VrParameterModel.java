package com.eveb.gateway.game.vr.model;

import com.eveb.gateway.game.model.TGmApi;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class VrParameterModel {
    /**
     * 通用参数父类
     */
    @Data
    public static class ParamModel {
        protected TGmApi api;
        protected String userName;
        protected String url;

        public ParamModel() {

        }

        public ParamModel(TGmApi api, String userName, String url) {
            this.api = api;
            this.userName = userName;
            this.url = url;
        }
    }

    /**
     * 通用参数
     */
    @Data
    public static class CommonParamModel extends ParamModel {

        public CommonParamModel() {

        }

        public CommonParamModel(TGmApi api, String userName, String url) {
            super(api, userName, url);
        }
    }

    /**
     * 登陆参数
     */
    @Data
    public static class LoginParamModel extends ParamModel {
        private Date loginDate;

        public LoginParamModel() {

        }

        public LoginParamModel(TGmApi api, String userName, Date loginDate) {
            super(api, userName, null);
            this.loginDate = loginDate;
        }
    }

    /**
     * 转账参数
     */
    @Data
    public static class TransferParamModel extends ParamModel {
        private String serialNumber;
        private Integer type;
        private BigDecimal amount;
        private Date createTime;

        public TransferParamModel() {

        }
    }
    /**
     * 确认转账参数
     */
    @Data
    public static class CheckTransferParamModel extends ParamModel {
        private String serialNumber;
        private Date startTime;
        private Date endTime;
        private Integer recordPage;
        private Integer recordCountPerPage;

        public CheckTransferParamModel() {

        }
    }

}
