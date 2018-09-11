package com.eveb.gateway.game.mg.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.mg.model.MgConstants;
import com.eveb.gateway.game.mg.model.MgParameterModel;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.game.unity.model.LoginModel;
import com.eveb.gateway.game.unity.model.PlayGameModel;
import com.eveb.gateway.game.unity.model.RegisterModel;
import com.eveb.gateway.game.unity.model.TransferModel;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MgService implements DepotService {

    @Autowired
    public OkHttpService okHttpService;
    @Autowired
    private SysService sysService;
    private static final String DEPOT_NAME = PlatFromEnum.ENUM_MG.getKey();

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
        try {
            TGmApi api = sysService.getApiBySiteCode(register.getSiteCode(), DEPOT_NAME);
            MgParameterModel.MgCreation creation = new MgParameterModel.MgCreation();
            creation.setCrId(api.getSecureCodes().get(MgConstants.Json.crId));
            creation.setCrType(api.getSecureCodes().get(MgConstants.Json.crType));
            creation.setNeId(api.getSecureCodes().get(MgConstants.Json.neId));
            creation.setNeType(api.getSecureCodes().get(MgConstants.Json.neType));
            creation.setTarType(MgConstants.MgCreationVal.tarType);
            creation.setUsername(api.getPrefix() + register.getUserName());
            creation.setName(register.getUserName());
            creation.setPassword(register.getPassword());
            creation.setConfirmPassword(register.getPassword());
            creation.setCurrency(MgConstants.MgCreationVal.currency);
            creation.setLanguage(MgConstants.MgCreationVal.language);
            creation.setEmail("");
            creation.setMobile("");
            creation.setPoker(new MgParameterModel.MgCreation.Poker(Boolean.FALSE));
            creation.setCasino(new MgParameterModel.MgCreation.Casino(Boolean.FALSE));
//            /**注册时所使用的账户密码**/
            api.setAgyAcc(api.getSecureCodes().get(MgConstants.Json.j_username));
            api.setMd5Key(api.getSecureCodes().get(MgConstants.Json.j_password));
            return okHttpService.putJson(okHttpService.initMgClient(getToken(api)), api.getPcUrl() + (MgConstants.Mod.createMember.replace(MgConstants.URL_ID, api.getSecureCodes().get(MgConstants.Json.crId))), JSON.toJSONString(creation));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object login(LoginModel login) {
        Map<String, String> map = new HashMap<String, String>();
        TGmApi api = sysService.getApiBySiteCode(login.getSiteCode(), DEPOT_NAME);
        MgParameterModel.MgLogin mgLogin = new MgParameterModel.MgLogin();
        mgLogin.setTimestamp(DateFormat.getUTCTimeStr());
        mgLogin.setApiusername(api.getSecureCodes().get(MgConstants.Json.apiAdmin));
        mgLogin.setApipassword(api.getSecureCodes().get(MgConstants.Json.apiPwd));
        mgLogin.setUsername(api.getPrefix() + login.getUserName());
        mgLogin.setPassword(login.getPassword());
        mgLogin.setPartnerId(api.getSecureCodes().get(MgConstants.Json.partnerId));
        mgLogin.setCurrencyCode(MgConstants.MgCreationVal.currency);
        GameXmlUtil.mgParseRes(map,okHttpService.postXml(okHttpService.initMgClient(getToken(api)),api.getPcUrl()+MgConstants.Mod.login,mgLogin.toString()));
        return map;
    }

    @Override
    public Object logout(LoginModel login) {
        return null;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        return transfer(transfer,MgConstants.TransferParam.deposit);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return transfer(transfer,MgConstants.TransferParam.withdraw);
    }

    private Object transfer(TransferModel transferModel,String transferType){
        LoginModel login=new LoginModel();
        login.setUserName(transferModel.getUserName());
        login.setSiteCode(transferModel.getSiteCode());
        Map loginMap=(Map) login(login);
        TGmApi api = sysService.getApiBySiteCode(transferModel.getSiteCode(), DEPOT_NAME);
        MgParameterModel.MgTransfer transfer = new MgParameterModel.MgTransfer();
        transfer.setTimestamp(DateFormat.getUTCTimeStr());
        transfer.setApiusername(api.getSecureCodes().get(MgConstants.Json.apiAdmin));
        transfer.setApipassword(api.getSecureCodes().get(MgConstants.Json.apiPwd));
        transfer.setToken(loginMap.get(MgConstants.LoginResKey.token).toString());
        transfer.setProduct(MgConstants.Product.casino);
        transfer.setOperation(transferType);
        transfer.setAmount(transferModel.getAmount().toString());
        transfer.setTxId(transferModel.getOrderNo());
        return okHttpService.postXml(okHttpService.initMgClient(getToken(api)),api.getPcUrl()+MgConstants.Mod.transfer,transfer.toString());
    }

    @Override
    public Object queryBalance(LoginModel login)
    {
        Map loginMap=(Map) login(login);
        TGmApi api = sysService.getApiBySiteCode(login.getSiteCode(), DEPOT_NAME);
        MgParameterModel.Balance balance=new MgParameterModel.Balance();
        balance.setApiusername(api.getSecureCodes().get(MgConstants.Json.apiAdmin));
        balance.setApipassword(api.getSecureCodes().get(MgConstants.Json.apiPwd));
        balance.setTimestamp(TimeUtil.getUTCTimeStr());
        balance.setToken(loginMap.get(MgConstants.LoginResKey.token).toString());
        return okHttpService.postXml(okHttpService.initMgClient(getToken(api)),api.getPcUrl()+MgConstants.Mod.balance,balance.toString());
    }

    @Override
    public Object checkTransfer(TransferModel transferModel) {
        return null;
    }

    @Override
    public  String getToken(){
        return null;
    }

    public  String getToken(TGmApi api){
        String url=String.format(api.getPcUrl()+MgConstants.Mod.token,api.getAgyAcc(),api.getMd5Key());
        String loginmsg= null;
        try {
            loginmsg = okHttpService.postJson(okHttpService.initMgClient(""),url,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map= (Map) JSON.parse(loginmsg);
        return map.get(MgConstants.TOKEN_RES_KEY).toString();
    }
}
