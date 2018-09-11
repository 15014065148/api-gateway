package com.eveb.gateway.game.mg.processor;

import com.eveb.gateway.constants.ErrMsgConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.ElasticService;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.utils.RequestParameter;
import com.eveb.gateway.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MgProcessor {

    @Autowired
    private SysService sysService;
    @Autowired
    private ElasticService elasticService;
    @Autowired
    private OkHttpService okHttpService;
    private static final String STR_PARTNERID = "partnerId=\"";
    private static final String MAPKEY_P_USM = "j_username";
    private static final String MAPKEY_P_PWD = "j_password";
    private static final String HEADKEY_TOKEN = "X-Api-Auth";

    public String processor(HttpServletRequest request) {
        String rurl = request.getRequestURI();
        int partIndex;
        String para;
        try {
            para = RequestParameter.reader(request);
        } catch (Exception e) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        partIndex = para.indexOf(STR_PARTNERID) + STR_PARTNERID.length();
        String partnerId = para.substring(partIndex, para.indexOf("\"", partIndex));
        TGmApi api = sysService.findTGApiByDepotAndSecure(PlatFromEnum.ENUM_MG.getValue(), partnerId);
        if (api == null) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            return okHttpService.postXml(api.getPcUrl() + rurl.substring(1), para);
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(), api.getAgyAcc(), PlatFromEnum.ENUM_MG.getValue(), para.substring(para.indexOf("<") + 1, para.indexOf(" ")), para, new Date());
        }
    }

    public String processorToken(HttpServletRequest request) {
        String rurl = request.getRequestURI();
        Map<String, String> para = RequestParameter.getPara(request);
        String agyacc = para.get(MAPKEY_P_USM);
        TGmApi api = sysService.findTGApiByDepotAndAgy(PlatFromEnum.ENUM_MG.getValue(), agyacc);
        if (api == null) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            return okHttpService.postJson(okHttpService.initMgClient(""), api.getPcUrl() + rurl.substring(1) + "?" + para.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors.joining("&")), "");
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(), api.getAgyAcc(), PlatFromEnum.ENUM_MG.getValue(), "j_spring_security_check", para.toString(), new Date());
        }
    }

    public String processorSecure(HttpServletRequest request) {
        String token = RequestParameter.getHead(request, HEADKEY_TOKEN);
        String rurl = request.getRequestURI().substring(1);
        String horId = Arrays.stream(rurl.split("/")).filter(id -> StringUtil.isNumeric(id)).collect(Collectors.joining());
        TGmApi api = sysService.findTGApiByDepotAndSecure(PlatFromEnum.ENUM_MG.getValue(), horId);
        if (api == null) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            return okHttpService.get(okHttpService.initMgClient(token), api.getPcUrl() + rurl);
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(), api.getAgyAcc(), PlatFromEnum.ENUM_MG.getValue(), "", rurl, new Date());
        }
    }
}
