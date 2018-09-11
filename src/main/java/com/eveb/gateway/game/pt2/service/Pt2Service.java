package com.eveb.gateway.game.pt2.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.game.unity.model.LoginModel;
import com.eveb.gateway.game.unity.model.PlayGameModel;
import com.eveb.gateway.game.unity.model.RegisterModel;
import com.eveb.gateway.game.unity.model.TransferModel;
import com.eveb.gateway.game.unity.service.DepotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class Pt2Service implements DepotService {

    @Autowired
    public OkHttpService okHttpService;
    @Autowired
    private SysService sysService;
    private static final String DEPOT_NAME = PlatFromEnum.ENUM_PT2.getKey();

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        TGmApi api = sysService.getApiBySiteCode(register.getSiteCode(), DEPOT_NAME);
        Map<String, String> paramap=new HashMap();
        paramap.put("code","af8ybhtest0621");
        paramap.put("country","CN");
        paramap.put("currency","CNY");
        paramap.put("language","zh-cn");
        paramap.put("status","normal");
        paramap.put("test","true");
        String url="https://agaapi.playngonetwork.com:23219/CasinoGameService/v1/players";
        try {
            okHttpService.postJson(okHttpService.initPt2Client(getToken()),url,paramap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object login(LoginModel login) {
        return null;
    }

    @Override
    public Object logout(LoginModel login) {
        return null;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        return null;
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return null;
    }

    @Override
    public Object queryBalance(LoginModel login) {
        return null;
    }

    @Override
    public Object checkTransfer(TransferModel transferModel) {
        return null;
    }

    @Override
    public String getToken() {
        try {
            String loginurl = "https://api.m27613.com/v1/login";
            String secretKey = "2cb6b71e-af51-4653-952a-9ad880e02886";
            String username = "AF8API";
            String password = "AAbb1122";

            Map<String, String> paramap = new HashMap();
            paramap.put("secretKey", secretKey);
            paramap.put("username", username);
            paramap.put("password", password);
            String loginrs = okHttpService.postForm(okHttpService.proxyClient, loginurl, paramap);
            return ((Map) JSON.parseObject(loginrs)).get("accessToken").toString();
        } catch (Exception e) {
            log.error("获取PT2 Token失败！");
        }
        return "";
    }
}
