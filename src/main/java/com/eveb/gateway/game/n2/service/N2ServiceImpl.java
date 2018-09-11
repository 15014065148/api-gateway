package com.eveb.gateway.game.n2.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.n2.model.N2Constants;
import com.eveb.gateway.game.n2.model.N2ParameterModel;
import com.eveb.gateway.game.n2.model.N2ResultModel;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.RedisService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.utils.SnowFlake;
import com.eveb.gateway.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/29 10:36
 **/
@Slf4j
@Service
public class N2ServiceImpl implements DepotService {

    @Autowired
    private OkHttpService okHttpService;
    @Autowired
    private RedisService redisService;

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        TGmApi tg=playGame.getTGmApi();
        String url;
        if(ApplicationConstants.ORIGIN_APP.equals(playGame.getOrigin())){
             url=String.format(N2Constants.MethodConstants.PLAYGAME_APP,tg.getMd5Key(),N2Constants.LAN,tg.getPrefix()+playGame.getUserName(),System.currentTimeMillis(),"","gm");
            return new UnityResultModel(Boolean.TRUE,url);
        }
         url=String.format(N2Constants.MethodConstants.PLAYGAME_PC,tg.getMd5Key(),N2Constants.LAN,tg.getPrefix()+playGame.getUserName(),System.currentTimeMillis(),"","gm");
        return new UnityResultModel(Boolean.TRUE,url);
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        return null;
    }

    @Override
    public Object login(LoginModel login) {
        N2ParameterModel.LoginModel lg=new N2ParameterModel.LoginModel();
        TGmApi tg=login.getTGmApi();
        lg.setId(new SnowFlake().nextId()+"");
        lg.setUserid(tg.getPrefix()+login.getUserName());
        lg.setPassword(login.getPassword());
        redisService.setRedisValue(ApplicationConstants.REIDS_SESSION_KEY+":"+PlatFromEnum.ENUM_N2.getKey()+"_"+lg.getUserid(),login.getTGmApi());
        String url=tg.getApiUrl()+String.format(N2Constants.MethodConstants.LOGIN,tg.getMd5Key(),N2Constants.LAN,lg.getUserid(),lg.getId());
        try {
            okHttpService.post(okHttpService.proxyClient,url,"");
        } catch (Exception e) {
            return new UnityResultModel(Boolean.FALSE,ApplicationConstants.ACTION_ERROR);
        }
        return new UnityResultModel(Boolean.TRUE,"");
    }

    @Override
    public Object logout(LoginModel login) {
        return null;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        TGmApi tg = transfer.getTGmApi();
        N2ParameterModel.TransferModel tf = new N2ParameterModel.TransferModel();
        tf.setAction(N2Constants.MethodConstants.ACTION_DEPOSIT);
        tf.setUserid(tg.getPrefix() + transfer.getUserName());
        tf.setVendorid(tg.getAgyAcc());
        tf.setMerchantpasscode(tg.getMd5Key());
        tf.setAmount(transfer.getAmount());
        tf.setCurrencyid(N2Constants.CNY);
        tf.setRefno(transfer.getOrderNo());
        /**存款使用D开头**/
        tf.setId("D" + new SnowFlake().nextId());
        String url = tg.getApiUrl() + N2Constants.MethodConstants.DEPOSIT;
        N2ResultModel.TransferModel rstf = transfer(tf, url);
        if (N2Constants.CheckLoginConstans.STATUSCODE_0.equals(rstf.getStatus())) {
            rstf.setVendorid(tg.getAgyAcc());
            rstf.setMerchantpasscode(tg.getMd5Key());
            rstf.setId(tf.getId());
            okHttpService.postXml(okHttpService.proxyClient, url, rstf.toString());
            return new UnityResultModel(Boolean.TRUE,tf.getId());
        }
        return new UnityResultModel(Boolean.FALSE, rstf.getErrdesc());
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        TGmApi tg = transfer.getTGmApi();
        N2ParameterModel.TransferModel tf = new N2ParameterModel.TransferModel();
        tf.setAction(N2Constants.MethodConstants.ACTION_WITHDRAWAL);
        tf.setUserid(tg.getPrefix() + transfer.getUserName());
        tf.setVendorid(tg.getAgyAcc());
        tf.setMerchantpasscode(tg.getMd5Key());
        tf.setAmount(transfer.getAmount());
        tf.setCurrencyid(N2Constants.CNY);
        tf.setRefno(transfer.getOrderNo());
        /**存款使用D开头**/
        tf.setId("W" + new SnowFlake().nextId());
        String url = tg.getApiUrl() + N2Constants.MethodConstants.WITHDRAWAL;
        N2ResultModel.TransferModel rstf = transfer(tf, url);
        if (N2Constants.CheckLoginConstans.STATUSCODE_0.equals(rstf.getStatus())) {
            return new UnityResultModel(Boolean.TRUE, rstf.getId());
        }
        return new UnityResultModel(Boolean.FALSE, rstf.getErrdesc());
    }

    @Override
    public Object queryBalance(LoginModel login) {
        TGmApi tg = login.getTGmApi();
        N2ParameterModel.BalanceModel bal=new N2ParameterModel.BalanceModel();
        bal.setUserid(tg.getPrefix()+login.getUserName());
        bal.setId("C"+new SnowFlake().nextId());
        bal.setVendorid(tg.getAgyAcc());
        bal.setMerchantpasscode(tg.getMd5Key());
        bal.setCurrencyid(N2Constants.CNY);
        String rsXml=okHttpService.postXml(okHttpService.proxyClient,tg.getApiUrl()+N2Constants.MethodConstants.BALANCE,bal.toString());
        log.info(rsXml);
        rsXml=rsXml.substring(rsXml.indexOf("<result"),rsXml.indexOf("</result>")+"</result>".length());
        N2ResultModel.BalanceModel rsbal=JSON.parseObject(JSON.toJSONString(XmlUtil.n2JSON(rsXml)),N2ResultModel.BalanceModel.class);
        if (rsbal.getBalance()!=null) {
            return new UnityResultModel(Boolean.TRUE,rsbal.getBalance());
        }
        return new UnityResultModel(Boolean.FALSE,rsbal.getErrdesc());
    }

    @Override
    public Object checkTransfer(TransferModel transfer) {
        return null;
    }

    @Override
    public Object getToken() {
        return null;
    }

    private N2ResultModel.TransferModel transfer(N2ParameterModel.TransferModel transfer,String url){
        String rsXml=okHttpService.postXml(okHttpService.proxyClient,url,transfer.toString());
        log.info(rsXml);
        rsXml=rsXml.substring(rsXml.indexOf("<result"),rsXml.indexOf("</result>")+"</result>".length());
        return  JSON.parseObject(JSON.toJSONString(XmlUtil.n2JSON(rsXml)),N2ResultModel.TransferModel.class);
    }

    public String checklogin(String xml) {
        log.info(xml);
        xml = xml.replace("<?xml version=\"1.0\" encoding=\"utf-16\"?>", "");
        Map map = XmlUtil.n2JSON(xml);
        if(map==null){
            N2ResultModel.CheckLoginModel cl=new N2ResultModel.CheckLoginModel();
            cl.setStatus(N2Constants.CheckLoginConstans.STATUS_FAIL);
            cl.setStatusCode(N2Constants.CheckLoginConstans.STATUSCODE_001);
            return cl.toString();
        }
        String key = PlatFromEnum.ENUM_N2.getKey() + "_" + map.get("userid");
        TGmApi gmApi =(TGmApi) redisService.getRedisValus(ApplicationConstants.REIDS_SESSION_KEY+":"+key);
        N2ResultModel.CheckLoginModel lg=JSON.parseObject(JSON.toJSONString(map),N2ResultModel.CheckLoginModel.class);
        if (gmApi != null) {
            redisService.del(ApplicationConstants.REIDS_SESSION_KEY+":"+key);
            lg.setStatus(N2Constants.CheckLoginConstans.STATUS_SUCCESS);
            lg.setVendorid(gmApi.getAgyAcc());
            lg.setMerchantpasscode(gmApi.getMd5Key());
            lg.setStatusCode(N2Constants.CheckLoginConstans.STATUSCODE_0);
        }else
        {
            lg.setStatus(N2Constants.CheckLoginConstans.STATUS_FAIL);
            lg.setStatusCode(N2Constants.CheckLoginConstans.STATUSCODE_001);
        }
        return lg.toString();
    }

}
