package com.eveb.gateway.game.agin.service;

import com.eveb.gateway.game.agin.model.AginConstants;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.model.LoginModel;
import com.eveb.gateway.game.unity.model.PlayGameModel;
import com.eveb.gateway.game.unity.model.RegisterModel;
import com.eveb.gateway.game.unity.model.TransferModel;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.utils.DESEncrypt;
import com.eveb.gateway.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AginService implements DepotService {

    @Autowired
    public OkHttpService okHttpService;



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
        register.getTGmApi().setPcUrl(register.getTGmApi().getPcUrl() + AginConstants.AGIN_M_DOBUSINESS);
        register.setUserName(register.getTGmApi().getPrefix() + register.getUserName());
        String para=String.format(AginConstants.AGIN_FUN_CHECKORCREATEGAMEACCOUT,register.getTGmApi().getAgyAcc(),register.getUserName());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(register.getTGmApi().getPcUrl());
        stringBuffer.append(getParams(register.getTGmApi().getSecureCodes().get(AginConstants.AGIN_DESCODE_KEY), register.getTGmApi().getMd5Key(), para));
        try {
            return okHttpService.post(okHttpService.proxyClient,stringBuffer.toString(),"");
        } catch (Exception e) {
            log.error(e.getMessage());
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
        return transferCheck(transfer,AginConstants.AGIN_FUN_DEPOSIT);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return transferCheck(transfer,AginConstants.AGIN_FUN_WITHDRAW);
    }

    public Object transferCheck(TransferModel transfer,String type) {
        Integer flag = 1;
        transfer.getTGmApi().setPcUrl(transfer.getTGmApi().getPcUrl() + AginConstants.AGIN_M_DOBUSINESS);
        transfer.setOrderNo(transfer.getTGmApi().getAgyAcc() + transfer.getOrderNo());
        transfer.setUserName(transfer.getTGmApi().getPrefix() + transfer.getUserName());
        String para = String.format(AginConstants.AGIN_FUN_PREPARETRANSFERCREDIT, transfer.getTGmApi().getAgyAcc(), transfer.getUserName(), transfer.getOrderNo(), type, transfer.getAmount());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(transfer.getTGmApi().getPcUrl());
        stringBuffer.append(getParams(transfer.getTGmApi().getSecureCodes().get(AginConstants.AGIN_DESCODE_KEY), transfer.getTGmApi().getMd5Key(), para));
        try {
            String rs = okHttpService.post(okHttpService.proxyClient, stringBuffer.toString(), "");
            log.info(rs);
            if (rs.contains("error")) {
                flag = 0;
            }
        } catch (Exception e) {
            flag = 0;
            log.error(e.getMessage());
        }
        para = String.format(AginConstants.AGIN_FUN_TRANSFERCREDITCONFIRM, transfer.getTGmApi().getAgyAcc(), transfer.getUserName(), transfer.getOrderNo(), type, transfer.getAmount(), flag);
        stringBuffer = new StringBuffer();
        stringBuffer.append(transfer.getTGmApi().getPcUrl());
        stringBuffer.append(getParams(transfer.getTGmApi().getSecureCodes().get(AginConstants.AGIN_DESCODE_KEY), transfer.getTGmApi().getMd5Key(), para));
        try {
            return okHttpService.post(okHttpService.proxyClient, stringBuffer.toString(), "");
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    @Override
    public Object queryBalance(LoginModel login) {
        login.getTGmApi().setPcUrl(login.getTGmApi().getPcUrl() + AginConstants.AGIN_M_DOBUSINESS);
        login.setUserName(login.getTGmApi().getPrefix() + login.getUserName());
        String para=String.format(AginConstants.AGIN_FUN_GETBALANCE,login.getTGmApi().getAgyAcc(),login.getUserName());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(login.getTGmApi().getPcUrl());
        stringBuffer.append(getParams(login.getTGmApi().getSecureCodes().get(AginConstants.AGIN_DESCODE_KEY), login.getTGmApi().getMd5Key(), para));
        try {
            return okHttpService.post(okHttpService.proxyClient,stringBuffer.toString(),"");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object checkTransfer(TransferModel transferModel) {
        transferModel.getTGmApi().setPcUrl(transferModel.getTGmApi().getPcUrl() + AginConstants.AGIN_M_DOBUSINESS);
        String para=String.format(AginConstants.AGIN_FUN_GETBALANCE,transferModel.getTGmApi().getAgyAcc(),transferModel.getTGmApi().getAgyAcc() + transferModel.getOrderNo());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(transferModel.getTGmApi().getPcUrl());
        stringBuffer.append(getParams(transferModel.getTGmApi().getSecureCodes().get(AginConstants.AGIN_DESCODE_KEY), transferModel.getTGmApi().getMd5Key(), para));
        try {
            return okHttpService.post(okHttpService.proxyClient,stringBuffer.toString(),"");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object getToken() {
        return null;
    }

    private static String getParams(String secureCode, String md5Key, String para) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            DESEncrypt d = new DESEncrypt(secureCode);
            String params = d.encrypt(para);
            String key = MD5.getMD5(params + md5Key);
            stringBuffer.append("params=").append(params).append("&key=").append(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return stringBuffer.toString();
    }
}
