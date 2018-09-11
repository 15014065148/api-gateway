package com.eveb.gateway.game.pt.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ErrMsgConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.RequestLog;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.ElasticService;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.utils.RequestParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class PtProcessor {

    private static final String X_ENTITY_KEY = "X_ENTITY_KEY";

    @Autowired
    private SysService sysService;
    @Autowired
    private ElasticService elasticService;
    @Autowired
    private OkHttpService okHttpService;

    public String processor(HttpServletRequest request) {
        RequestLog rlog = new RequestLog();
        String key = RequestParameter.getHead(request,X_ENTITY_KEY);
        String methon = request.getRequestURI();
        if (key.isEmpty()) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        String keys = key;
        TGmApi api = sysService.findTGApiByDepotAndSecure(PlatFromEnum.ENUM_PT.getValue(), keys);
        if (api == null) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            /**写入日志*/
//            rlog.setAgentAccount(api.getAgyAcc());
            rlog.setApiName(methon.substring(methon.indexOf("/", 1) + 1, methon.indexOf("/", methon.indexOf("/", 1) + 1)));
            return okHttpService.post(okHttpService.initPtClient((((Map) JSON.parse(api.getSecureCode()))).get(X_ENTITY_KEY).toString()), api.getPcUrl() + methon, "");

        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(rlog);
        }
    }
}
