package com.eveb.gateway.game.cq9.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RsMbrTransferRecordModel {

    private MbrTransferRecordModel.Data data;
    private MbrTransferRecordModel.Status status;

    @lombok.Data
    public static class Data{
        private String _id;
        /**該交易的動作 如 bet, win , debit, credit ,payoff*/
        private String action;
        private MbrTransferRecordModel.Data.Target target;
        private MbrTransferRecordModel.Data.Status status;
        private BigDecimal before;
        private BigDecimal balance;
        private String currency;
        private MbrTransferRecordModel.Data.Event event;

        public void setId(String id){
            this._id=id;
        }

        @lombok.Data
        public static class Target{
            private String account;

            public Target() {
            }

            public Target(String account) {
                this.account = account;
            }
        }
        @lombok.Data
        public static class Status{
            private String createtime;
            private String endtime;
            private String status;

            public Status() {
            }

            public Status(String createtime, String endtime, String status) {
                this.createtime = createtime;
                this.endtime = endtime;
                this.status = status;
            }
        }
        @lombok.Data
        public static class Event{
            private String mtcode;
            private BigDecimal amount;
            private String eventtime;

            public Event() {
            }

            public Event(String mtcode, BigDecimal amount, String eventtime) {
                this.mtcode = mtcode;
                this.amount = amount;
                this.eventtime = eventtime;
            }
        }
    }

    @lombok.Data
    public static class Status{
        private String code="200";
        private String message;
        private String datetime;

        public Status() {
        }

        public Status(String message, String datetime) {
            this.message = message;
            this.datetime = datetime;
        }

        public Status(String code, String message, String datetime) {
            this.code = code;
            this.message = message;
            this.datetime = datetime;
        }
    }
}
