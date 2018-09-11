package com.eveb.gateway.game.agin.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ErrMsgConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.ElasticService;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.utils.DESEncrypt;
import com.eveb.gateway.utils.RequestParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AginProcessor {

    @Autowired
    private SysService sysService;
    @Autowired
    private ElasticService elasticService;
    @Autowired
    private OkHttpService okHttpService;
    private static final String MAPKEY_PARAMS = "params";
    private static final String MAPKEY_DESCODE = "desCode";
    private static final String MAPKEY_CAGENT = "cagent=";

    public String processor(HttpServletRequest request) {
        String cagent = "";
        String methon = request.getRequestURI();
        methon = methon.substring(methon.lastIndexOf("/") + 1);
        Map<String, String> parmMap =RequestParameter.getPara(request);
        /**针对url请求时+号变空的处理*/
        String params = parmMap.get(MAPKEY_PARAMS).replace(" ", "+");
        TGmApi api = new TGmApi();
        List<TGmApi> apis = sysService.findGmApiList(PlatFromEnum.ENUM_AGIN.getValue());
        for (TGmApi a : apis) {
            Map secureMap = JSON.parseObject(a.getSecureCode());
            DESEncrypt d = new DESEncrypt(secureMap.get(MAPKEY_DESCODE).toString());
            try {
                params = d.decrypt(params);
                api = a;
                continue;
            } catch (Exception e) {
            }
        }
        cagent= Arrays.stream(params.split("/")).filter(p -> p.startsWith(MAPKEY_CAGENT)).map(p -> p.replace(MAPKEY_CAGENT, "")).collect(Collectors.joining());
        try {
            return okHttpService.post(okHttpService.proxyClient, api.getPcUrl() + methon + "?" + parmMap.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors
                    .joining("&")), "");
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(),cagent,PlatFromEnum.ENUM_AGIN.getValue(),methon,parmMap.toString(),new Date());
        }
    }
}
