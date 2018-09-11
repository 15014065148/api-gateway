package com.eveb.gateway.game.bbin.processor;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ErrMsgConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.ElasticService;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.utils.RequestParameter;
import com.eveb.gateway.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Component
public class BbinProcessor {

    @Autowired
    private OkHttpService okHttpService;
    @Autowired
    private SysService sysService;
    @Autowired
    private ElasticService elasticService;

    private static final String MAPKEY_UPPERNAME = "uppername";
    /**
     * 跳转到域名的方法集合
     **/
    private static final String METHONLIST = "Login,Login2,PlayGame,PlayGameByH5";

    /***
     * 针对BBIN JSON格式处理请求
     * @param request
     * @return
     */
    public String jsonProcessor(HttpServletRequest request) {
        String methon = request.getRequestURI();
        methon = methon.substring(methon.lastIndexOf("/") + 1);
        Map<String, String> parmMap = RequestParameter.getPara(request);
        String uppername = parmMap.get(MAPKEY_UPPERNAME);
        TGmApi api = sysService.findTGApiByDepotAndAgy(PlatFromEnum.ENUM_BBIN.getValue(), uppername);
        if (api == null) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            if (METHONLIST.contains(methon)) {
                return okHttpService.postForm(okHttpService.proxyClient, api.getPcUrl2() + methon, parmMap);
            } else {
                return okHttpService.postForm(okHttpService.proxyClient, api.getPcUrl() + methon, parmMap);
            }
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(), uppername, PlatFromEnum.ENUM_BBIN.getValue(), methon, parmMap.toString(), new Date());
        }
    }

    /***
     * 针对BBIN XML格式处理请求
     * @param request
     * @return
     * @throws Exception
     */
    public String xmlProcessor(HttpServletRequest request) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String str, paraStr = "";
        while ((str = reader.readLine()) != null) {
            paraStr += str;
        }
        str = paraStr;
        Map parmMap = XmlUtil.xml2JSON(paraStr.getBytes());
        log.info(parmMap.toString());
        str = ((List<Map>) ((Map) parmMap.get("request")).get("element")).get(0).toString().replace("[", "").replace("]", "");
        parmMap = (Map) JSON.parse(str);
        String uppername = parmMap.get(MAPKEY_UPPERNAME) == null ? "" : parmMap.get(MAPKEY_UPPERNAME).toString();
        TGmApi api = sysService.findTGApiByDepotAndAgy(PlatFromEnum.ENUM_BBIN.getValue(), uppername);
        if (api == null) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            return okHttpService.post(okHttpService.proxyClient, api.getPcUrl().replace("JSON", "XML"), paraStr);
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(), uppername, PlatFromEnum.ENUM_BBIN.getValue(), paraStr.replace(" ", "").substring(paraStr.replace(" ", "").indexOf("action=\"") + "action=\"".length(), paraStr.replace(" ", "").indexOf("\"><element>")), parmMap.toString(), new Date());
        }
    }

}
