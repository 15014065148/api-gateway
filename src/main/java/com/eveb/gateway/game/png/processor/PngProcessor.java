package com.eveb.gateway.game.png.processor;

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
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class PngProcessor {

    @Autowired
    private SysService sysService;
    @Autowired
    private ElasticService elasticService;
    @Autowired
    private OkHttpService okHttpService;
    private static final String MAPKEY_AUTHOR = "Authorization";
    private static final String MAPKEY_SOAP = "SOAPAction";
    private static final String STR_BASIC = "Basic ";

    public String processor(HttpServletRequest request) {
        Map hendMap = RequestParameter.getHead(request);
        String para;
        String rurl = hendMap.get(MAPKEY_SOAP.toUpperCase()).toString();
        try {
            para = RequestParameter.reader(request);
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        }
        String agyacc = (new String(Base64.getDecoder().decode(hendMap.get(MAPKEY_AUTHOR.toUpperCase()).toString().replace(STR_BASIC, "")))).split(":")[0];
        TGmApi api = sysService.findTGApiByDepotAndAgy(PlatFromEnum.ENUM_PNG.getValue(), agyacc);
        if (api == null) {
            return ErrMsgConstants.ERR_NO_UPPERNAME;
        }
        try {
            return okHttpService.postXml(api.getPcUrl(), para, hendMap);
        } catch (Exception e) {
            return ErrMsgConstants.ERR_MSG;
        } finally {
//            elasticService.insert(request.getLocalAddr(), api.getAgyAcc(), PlatFromEnum.ENUM_PNG.getValue(), rurl.substring(rurl.lastIndexOf("/") + 1), para, new Date());
        }
    }
}
