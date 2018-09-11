package com.eveb.gateway.game.png.service;

import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.png.model.PngBase64Code;
import com.eveb.gateway.game.png.model.PngConstants;
import com.eveb.gateway.game.png.model.PngParameterModel;
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
public class PngService implements DepotService {

    @Autowired
    public OkHttpService okHttpService;
    @Autowired
    private SysService sysService;
    private static final String DEPOT_NAME = PlatFromEnum.ENUM_PNG.getKey();

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
        PngParameterModel.CreateMember create=new PngParameterModel.CreateMember();
        create.setExternalUserId(api.getPrefix() + register.getUserName());
        create.setUsername(register.getUserName());
        create.setNickname(register.getUserName());
        create.setCurrency(PngConstants.Currencies.china);
        create.setCountry(PngConstants.Countrys.china);
        create.setRegistration(PngConstants.registerDate());
        create.setBrandId(api.getSecureCodes().get(PngConstants.JsonKey.brandId));
        create.setLanguage(PngConstants.Languages.chinese_S);
        create.setIp(PngConstants.DEF_IP);
        create.setLocked(Boolean.FALSE.toString());
        create.setGender(PngConstants.Gender.female);
        try {
            return okHttpService.postXml(okHttpService.initHeadClient(initHeand(api,PngConstants.Method.REGISTER)),api.getPcUrl(),create.toString());
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
        return transfer(transfer,PngConstants.Method.CREDIT);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return transfer(transfer,PngConstants.Method.DEBIT);
    }

    private Object transfer(TransferModel transferModel,String method){
        TGmApi api = sysService.getApiBySiteCode(transferModel.getSiteCode(), DEPOT_NAME);
        PngParameterModel.Transfer transfer=new PngParameterModel.Transfer();
        transfer.setAmount(transferModel.getAmount().toString());
        transfer.setCurrency(PngConstants.Currencies.china);
        transfer.setExternalTransactionId(transferModel.getOrderNo());
        transfer.setExternalUserId(api.getPrefix()+transferModel.getUserName());
        try {
            return okHttpService.postXml(api.getPcUrl(),transfer.toString(method),initHeand(api,method));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object queryBalance(LoginModel login) {
        TGmApi api = sysService.getApiBySiteCode(login.getSiteCode(), DEPOT_NAME);
        PngParameterModel.BalanceOrTicket balance=new PngParameterModel.BalanceOrTicket();
        balance.setExternalUserId(api.getPrefix()+login.getUserName());
        balance.setMod(PngConstants.Method.BALANCE);
        try {
            return okHttpService.postXml(api.getPcUrl(),balance.toString(),initHeand(api,PngConstants.Method.BALANCE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object checkTransfer(TransferModel transferModel) {
        return null;
    }

    private Map initHeand(TGmApi api,String methon){
        Map map=new HashMap();
        PngBase64Code png=new PngBase64Code();
        png.setUserName(api.getAgyAcc());
        png.setPassWd(api.getMd5Key());
        map.put(PngConstants.AUTHORIZATION,png.toString());
        map.put(PngConstants.SOAPACTION,PngConstants.BASIC_SOAPACTION +methon);
        return map;
    }

    @Override
    public Object getToken() {
        return null;
    }

}
