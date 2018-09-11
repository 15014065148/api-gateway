package com.eveb.gateway.game.ibc.processor;

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
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class IbcProcessor {

    @Autowired
    private OkHttpService okHttpService;
    @Autowired
    private SysService sysService;
    @Autowired
    private ElasticService elasticService;
    private static final String MAPKEY_OPCODE = "OpCode";

    public String processor(HttpServletRequest request) {
        Map<String, String> paraMap = RequestParameter.getPara(request);
        String rurl = request.getRequestURI();
        String opCode = paraMap.get(MAPKEY_OPCODE);
        TGmApi api = sysService.findTGApiByDepotAndAgy(PlatFromEnum.ENUM_IBC.getValue(), opCode);
        if (api == null) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            return okHttpService.get(okHttpService.proxyClient, api.getPcUrl() + rurl.substring(1) + "?" + paraMap.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors.joining("&")));
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(), api.getAgyAcc(), PlatFromEnum.ENUM_IBC.getValue(), rurl.substring(1).substring(rurl.indexOf("/")), paraMap.toString(), new Date());
        }
    }
}
