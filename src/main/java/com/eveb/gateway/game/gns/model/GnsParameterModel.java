package com.eveb.gateway.game.gns.model;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class GnsParameterModel {

    @Data
    public static class Transfer {
        private String user_id;
        private String partner_id;
        private Long credits;
        private String currency;
        private String custom_json;
        private String action;
        private String external_transaction_id;
    }
}
