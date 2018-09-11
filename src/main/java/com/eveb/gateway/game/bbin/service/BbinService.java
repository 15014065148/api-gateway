package com.eveb.gateway.game.bbin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.bbin.model.BbinParameterModel;
import com.eveb.gateway.game.cq9.model.RsModel;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BbinService implements DepotService {

    @Autowired
    public OkHttpService okHttpService;
    @Autowired
    private SysService sysService;
    private static final String DEPOT_NAME=PlatFromEnum.ENUM_BBIN.getKey();

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        TGmApi api = sysService.getApiBySiteCode(playGame.getSiteCode(), DEPOT_NAME);
        String[] keys = api.getSecureCodes().get(BbinParameterModel.BBIN_API_PLAYGAME).split(",");
        playGame.setUserName(api.getPrefix() + playGame.getUserName());
        BbinParameterModel.PlayGame parameter = new BbinParameterModel.PlayGame();
        parameter.setWebsite(api.getWebName());
        parameter.setUsername(playGame.getUserName());
        parameter.setGamekind(playGame.getGameType());   //游戏类型
        parameter.setGametype(playGame.getGameId());   //游戏id
        if("3".equals(playGame.getGameType())){
            parameter.setGamecode("1");
        }
        parameter.setKey(keys[0] + MD5.getMD5(api.getWebName() + playGame.getUserName() + keys[1] + BbinParameterModel.getValidDate()) + keys[2]);
        try {
            String rsstr = okHttpService.postForm(okHttpService.proxyClient, api.getPcUrl2() + BbinParameterModel.BBIN_API_PLAYGAME, (Map) JSON.toJSON(parameter));
            log.info(rsstr);
            return rsstr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        TGmApi api = sysService.getApiBySiteCode(register.getSiteCode(), DEPOT_NAME);
        String[] keys=api.getSecureCodes().get(BbinParameterModel.BBIN_API_CREATEMEMBER).split(",");
        register.setUserName(api.getPrefix() + register.getUserName());
        BbinParameterModel.Login parameter = new BbinParameterModel.Login();
        parameter.setWebsite(api.getWebName());
        parameter.setUppername(api.getAgyAcc());
        parameter.setUsername(register.getUserName());
        parameter.setPassword(ApplicationConstants.DEFAULT_USER_PASSWORD);
        parameter.setKey(keys[0] + MD5.getMD5(api.getWebName() + register.getUserName() + keys[1] + BbinParameterModel.getValidDate()) + keys[2]);

        UnityResultModel resultModel = new UnityResultModel();
        resultModel.setCode(Boolean.FALSE);
        String resultString = "";
        try {
            resultString = okHttpService.postForm(okHttpService.proxyClient, api.getPcUrl()+BbinParameterModel.BBIN_API_CREATEMEMBER, (Map) JSON.toJSON(parameter));
            log.error("创建会员返回值========"+resultString);
        } catch (Exception e) {
            log.error("调用接口异常");
            resultModel.setMessage("调用创建会员接口异常4444444444");
        }
        Map resultMaps = (Map) JSON.parse(resultString);
        Boolean status = Boolean.parseBoolean(resultMaps.get("result").toString());
        resultModel.setCode(status);
        Map maps = (Map) JSON.parse(resultMaps.get("data").toString());
        resultModel.setMessage(maps.get("Message"));
        return resultModel;
    }

    @Override
    public Object login(LoginModel login) {
        TGmApi api = sysService.getApiBySiteCode(login.getSiteCode(), DEPOT_NAME);
        String[] keys = api.getSecureCodes().get(BbinParameterModel.BBIN_API_LOGIN).split(",");
        login.setUserName(api.getPrefix() + login.getUserName());
        BbinParameterModel.Login parameter = new BbinParameterModel.Login();
        parameter.setWebsite(api.getWebName());
        parameter.setUppername(api.getAgyAcc());
        parameter.setUsername(login.getUserName());
        parameter.setPassword(login.getPassword());
        parameter.setKey(keys[0] + MD5.getMD5(api.getWebName() + login.getUserName() + keys[1] + BbinParameterModel.getValidDate()) + keys[2]);
        try {
            String rsstr = okHttpService.postForm(okHttpService.proxyClient, api.getPcUrl2() + BbinParameterModel.BBIN_API_LOGIN, (Map) JSON.toJSON(parameter));
            log.info(rsstr);
            return rsstr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object logout(LoginModel login) {
        TGmApi api = sysService.getApiBySiteCode(login.getSiteCode(), DEPOT_NAME);
        String[] keys = api.getSecureCodes().get(BbinParameterModel.BBIN_API_LOGOUT).split(",");
        login.setUserName(api.getPrefix() + login.getUserName());
        BbinParameterModel.Logout parameter = new BbinParameterModel.Logout();
        parameter.setWebsite(api.getWebName());
        parameter.setUsername(login.getUserName());
        parameter.setKey(keys[0] + MD5.getMD5(api.getWebName() + login.getUserName() + keys[1] + BbinParameterModel.getValidDate()) + keys[2]);

        UnityResultModel resultModel = new UnityResultModel();
        resultModel.setCode(Boolean.FALSE);
        String resultString;
        try {
            resultString = okHttpService.postForm(okHttpService.proxyClient, api.getPcUrl() + BbinParameterModel.BBIN_API_LOGOUT, (Map) JSON.toJSON(parameter));
        } catch (Exception e) {
            log.error("登出异常");
            resultModel.setMessage("登出异常");
            return resultModel;
        }
        Map resultMaps = (Map) JSON.parse(resultString);
        Boolean status = Boolean.parseBoolean(resultMaps.get("result").toString());
        resultModel.setCode(status);
        Map maps = (Map) JSON.parse(resultMaps.get("data").toString());
        resultModel.setMessage(maps.get("Message"));
        return resultModel;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        return transfer(transfer,BbinParameterModel.Transfer.IN);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return transfer(transfer,BbinParameterModel.Transfer.OUT);
    }

    @Override
    public Object queryBalance(LoginModel login) {
        TGmApi api = sysService.getApiBySiteCode(login.getSiteCode(), DEPOT_NAME);
        String[] keys=api.getSecureCodes().get(BbinParameterModel.BBIN_API_CHECKUSRBALANCE).split(",");
        login.setUserName(api.getPrefix() + login.getUserName());
        BbinParameterModel.Balance parameter=new BbinParameterModel.Balance();
        parameter.setWebsite(api.getWebName());
        parameter.setUppername(api.getAgyAcc());
        parameter.setUsername(login.getUserName());
        parameter.setKey(keys[0] + MD5.getMD5(api.getWebName() + login.getUserName() + keys[1] + BbinParameterModel.getValidDate()) +keys[2]);

        UnityResultModel resultModel = new UnityResultModel();
        resultModel.setCode(Boolean.FALSE);
        String resultString;
        try {
            resultString = okHttpService.postForm(okHttpService.proxyClient, api.getPcUrl()+BbinParameterModel.BBIN_API_CHECKUSRBALANCE, (Map)JSON.toJSON(parameter));
        } catch (Exception e) {
            resultModel.setMessage("查询用户余额异常");
            return resultModel;
        }
        Map resultMaps = (Map) JSON.parse(resultString);
         Boolean status = Boolean.parseBoolean(resultMaps.get("result").toString());

        JSONArray jsonArray = new JSONArray((List<Object>) resultMaps.get("data"));
        //resultModel.setMessage(Double.parseDouble(money)/100);
        resultModel.setCode(status);
        if(status){
            resultModel.setMessage(jsonArray.getJSONObject(0).getString("Balance"));
        }else {
            resultModel.setMessage(jsonArray.getJSONObject(0).getString("Message"));
        }
        return resultModel;
    }

    private Object transfer(TransferModel transfer,String tranferType){
        TGmApi api = sysService.getApiBySiteCode(transfer.getSiteCode(), DEPOT_NAME);
        String[] keys=api.getSecureCodes().get(BbinParameterModel.BBIN_API_TRANSFER).split(",");
        transfer.setUserName(api.getPrefix() + transfer.getUserName());
        BbinParameterModel.Transfer parameter=new BbinParameterModel.Transfer();
        parameter.setWebsite(api.getWebName());
        parameter.setUppername(api.getAgyAcc());
        parameter.setUsername(transfer.getUserName());
        parameter.setRemitno(transfer.getOrderNo());
        parameter.setAction(tranferType);
        parameter.setRemit(transfer.getAmount().intValue());
        parameter.setKey(keys[0] + MD5.getMD5(api.getWebName() + transfer.getUserName() +transfer.getOrderNo()+ keys[1] + BbinParameterModel.getValidDate()) +keys[2]);

        UnityResultModel resultModel = new UnityResultModel();
        resultModel.setCode(Boolean.FALSE);
        String resultString;
        try {
            resultString = okHttpService.postForm(okHttpService.proxyClient, api.getPcUrl()+BbinParameterModel.BBIN_API_TRANSFER, (Map)JSON.toJSON(parameter));
        } catch (Exception e) {
            resultModel.setMessage("用户转账异常");
            return resultModel;
        }
        Map resultMaps = (Map) JSON.parse(resultString);
        Boolean status = Boolean.parseBoolean(resultMaps.get("result").toString());
        resultModel.setCode(status);
        Map maps = (Map) JSON.parse(resultMaps.get("data").toString());
        resultModel.setMessage(maps.get("Message"));
        return resultModel;
    }

    @Override
    public Object checkTransfer(TransferModel transfer) {
        TGmApi api = sysService.getApiBySiteCode(transfer.getSiteCode(), DEPOT_NAME);
        String[] keys=api.getSecureCodes().get(BbinParameterModel.BBIN_API_CHECKTRANSFER).split(",");
        BbinParameterModel.CheckTransfer parameter=new BbinParameterModel.CheckTransfer();
        parameter.setWebsite(api.getWebName());
        parameter.setTransid(transfer.getOrderNo());
        parameter.setKey(keys[0] + MD5.getMD5(api.getWebName() + keys[1] + BbinParameterModel.getValidDate()) +keys[2]);

        UnityResultModel resultModel = new UnityResultModel();
        resultModel.setCode(Boolean.FALSE);
        String resultString;
        try {
            resultString = okHttpService.postForm(okHttpService.proxyClient, api.getPcUrl()+BbinParameterModel.BBIN_API_CHECKTRANSFER, (Map)JSON.toJSON(parameter));
        } catch (Exception e) {
            resultModel.setMessage("查询转账记录异常");
            return resultModel;
        }
        Map resultMaps = (Map) JSON.parse(resultString);
        Boolean status = Boolean.parseBoolean(resultMaps.get("result").toString());
        resultModel.setCode(status);
        Map maps = (Map) JSON.parse(resultMaps.get("data").toString());
        if(status){
            String transferStatus = maps.get("Status").toString();
            if("1".equals(transferStatus)){
                resultModel.setCode(Boolean.TRUE);
                resultModel.setMessage("成功");
            }else {
                resultModel.setMessage("处理中或失败");
            }
        } else {
            resultModel.setMessage(maps.get("Message"));
        }
        return resultModel;
    }

    @Override
    public Object getToken() {
        return null;
    }
}
