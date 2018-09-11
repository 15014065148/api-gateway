package com.eveb.gateway.game.pt2.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ErrMsgConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.ElasticService;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.utils.RequestParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Pt2Processor {

    @Autowired
    private SysService sysService;
    @Autowired
    private ElasticService elasticService;
    @Autowired
    private OkHttpService okHttpService;
    private static final String MAPKEY_SECRETKEY = "secretKey";
    private static final String MAPKEY_LOGINURL = "loginurl";
    private static final String X_ACCESS_TOKEN = "x-access-token";
    private static final String METHON_LOGIN = "login";

    public String processor(HttpServletRequest request) {
        if (!request.getRequestURI().contains(METHON_LOGIN)) {
            return processorOther(request);
        } else {
            return processorLogin(request);
        }
    }

    private String processorOther(HttpServletRequest request) {
        TGmApi api = new TGmApi();
        String token = RequestParameter.getHead(request, X_ACCESS_TOKEN);
        String methon = request.getRequestURI();
        api.setDepotName(PlatFromEnum.ENUM_PT2.getValue());
        api = sysService.findGmApiOne(api);
        if (token.isEmpty()) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            return okHttpService.get(okHttpService.initPt2Client(token), api.getPcUrl() + methon, "");
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(), null, PlatFromEnum.ENUM_PT2.getValue(), METHON_LOGIN, methon.toString(), new Date());
        }
    }

    private String processorLogin(HttpServletRequest request) {
        String para = "";
        try {
            para = RequestParameter.reader(request);
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        }
        Map<String, String> paramap = (Map<String, String>) JSON.parse(para);
        String secretKey = (paramap).entrySet().stream().filter(entry -> entry.getKey().equals(MAPKEY_SECRETKEY)).map(entry -> entry.getValue()).collect(Collectors.joining());
        TGmApi api = sysService.findTGApiByDepotAndSecure(PlatFromEnum.ENUM_PT2.getValue(), secretKey);
        if (api == null) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            return okHttpService.postJson(okHttpService.client, ((Map) JSON.parse(api.getSecureCode())).get(MAPKEY_LOGINURL).toString(), paramap);
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(), api.getAgyAcc(), PlatFromEnum.ENUM_PT2.getValue(), METHON_LOGIN, paramap.toString(), new Date());
        }
    }
}
