package com.eveb.gateway.game.vr.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.game.vr.model.CheckTransferResultModel;
import com.eveb.gateway.game.vr.model.VrConstants;
import com.eveb.gateway.game.vr.model.VrParameterModel;
import com.eveb.gateway.game.vr.model.VrResultModel;
import com.eveb.gateway.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class VrServiceImpl implements DepotService {

    @Autowired
    public OkHttpService okHttpService;

    @Autowired
    private VrResultModel vrResultModel;
    private SimpleDateFormat vrDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");



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
        return login(new VrParameterModel.LoginParamModel(playGame.getTGmApi(), getUserName(playGame), new Date()));
    }


    @Override
    public Object createPlayer(RegisterModel register) {
        return commonProcess(new VrParameterModel.CommonParamModel(register.getTGmApi(), getUserName(register), VrConstants.GREATEUSER_URL));
    }


    @Override
    public Object login(LoginModel login) {
        return login(new VrParameterModel.LoginParamModel(login.getTGmApi(), getUserName(login), new Date()));
    }


    @Override
    public Object logout(LoginModel login) {
        return commonProcess(new VrParameterModel.CommonParamModel(login.getTGmApi(), getUserName(login), VrConstants.USERLOGOUT_URL));
    }


    @Override
    public Object deposit(TransferModel transfer) {
        VrParameterModel.TransferParamModel transferModel = getTransferParamModel(transfer, VrConstants.USERDEPOSIT_URL, VrConstants.TransferConstants.DEPOSIT_TYPE);
        return transfer(transferModel);
    }



    @Override
    public Object withdrawal(TransferModel transfer) {
        VrParameterModel.TransferParamModel transferModel = getTransferParamModel(transfer, VrConstants.USERWITHDRAWAL_URL, VrConstants.TransferConstants.WITHDRAWAL_TYPE);
        return transfer(transferModel);
    }


    @Override
    public Object queryBalance(LoginModel login) {
        return commonProcess(new VrParameterModel.CommonParamModel(login.getTGmApi(), getUserName(login), VrConstants.USEBALANCE_URL));
    }


    @Override
    public Object checkTransfer(TransferModel transfer) {
        VrParameterModel.CheckTransferParamModel checkTransferParamModel = new VrParameterModel.CheckTransferParamModel();
        checkTransferParamModel.setApi(transfer.getTGmApi());
        checkTransferParamModel.setSerialNumber(transfer.getOrderNo());
        checkTransferParamModel.setEndTime(new Date());
        checkTransferParamModel.setStartTime(getTransferStartTime(checkTransferParamModel.getEndTime()));
        checkTransferParamModel.setRecordPage(VrConstants.TransferConstants.RECORDPAGE);
        checkTransferParamModel.setRecordCountPerPage(VrConstants.TransferConstants.RECORDCOUNTPERPAGE);
        checkTransferParamModel.setUrl(VrConstants.CHECKTRANSFER_URL);
        return checkTransfer(checkTransferParamModel);
    }

    @Override
    public Object getToken() {
        return null;
    }

    /**
     * 用户登陆/玩游戏
     */
    public UnityResultModel login(VrParameterModel.LoginParamModel vrLoginParameterModel) {
        String jumpUrl = requestLogin(vrLoginParameterModel);
        return vrResultModel.getLoginResult(jumpUrl);
    }

    /**
     * 新增用户/注销用户/查询余额
     */
    public UnityResultModel commonProcess(VrParameterModel.CommonParamModel vrCommonParamModel) {
        Map<String, Object> map = commonRequest(vrCommonParamModel);
        return vrResultModel.getCommonResult(map);
    }

    /**
     * 用户确认转账
     */
    public UnityResultModel checkTransfer(VrParameterModel.CheckTransferParamModel checkTransferParamModel) {
        CheckTransferResultModel checkTransferResultModel = checkTransferRequest(checkTransferParamModel);
        return vrResultModel.getcheckTransferResult(checkTransferResultModel);
    }

    /**
     * 用户存款
     */
    public UnityResultModel transfer(VrParameterModel.TransferParamModel transferModel) {
        Map<String, Object> map = transferRequest(transferModel);
        return vrResultModel.getTransferResult(map);
    }

    /**
     * 用户登陆
     */
    public String requestLogin(VrParameterModel.LoginParamModel vrLoginParameterModel) {
        try {
            String url = vrLoginParameterModel.getApi().getSecureCodes().get(VrConstants.USERLOGIN_URL);
            String key = vrLoginParameterModel.getApi().getMd5Key();
            String data = String.format("playerName=%s&loginTime=%s", vrLoginParameterModel.getUserName(), formatDate(vrLoginParameterModel.getLoginDate()));//2018-08-06T09:00:00Z
            data = AesUtil.aesEncrypt32(data, key);
            data = URLEncoder.encode(data, "utf-8");
            String jumpUrl = url + "?" + VrConstants.RequestConstants.VR_VERSION + "=" + vrLoginParameterModel.getApi().getProxyFore() + "&" + VrConstants.RequestConstants.VR_ID + "=" + vrLoginParameterModel.getApi().getAgyAcc() + "&" + VrConstants.RequestConstants.VR_DATA + "=" + data;
            return jumpUrl;
        } catch (Exception e) {
            log.info("登陆/游戏失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增用户/注销用户/查询余额
     */
    public Map<String, Object> commonRequest(VrParameterModel.CommonParamModel vrCommonParamModel) {
        try {
            String url = vrCommonParamModel.getApi().getSecureCodes().get(vrCommonParamModel.getUrl());
            String key = vrCommonParamModel.getApi().getMd5Key();
            Map<String, Object> map = new HashMap();
            map.put(VrConstants.RequestConstants.VR_VERSION, vrCommonParamModel.getApi().getProxyFore());
            map.put(VrConstants.RequestConstants.VR_ID, vrCommonParamModel.getApi().getAgyAcc());
            String data = String.format("{\"playerName\": \"%s\"}", vrCommonParamModel.getUserName());
            data = AesUtil.aesEncrypt32(data, key);//加密
            map.put(VrConstants.RequestConstants.VR_DATA, data);
            String result = okHttpService.postJson(okHttpService.proxyClient, url, map);
            result = AesUtil.aesDecrypt32(result, key);//解密
            return JSON.parseObject(result, Map.class);
        } catch (Exception e) {
            log.info("出现异常！");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用户转账
     */
    public Map<String, Object> transferRequest(VrParameterModel.TransferParamModel transferModel) {
        try {
            String url = transferModel.getApi().getSecureCodes().get(transferModel.getUrl());
            String key = transferModel.getApi().getMd5Key();
            Map<String, Object> map = new HashMap();
            map.put(VrConstants.RequestConstants.VR_VERSION, transferModel.getApi().getProxyFore());
            map.put(VrConstants.RequestConstants.VR_ID, transferModel.getApi().getAgyAcc());
            String data = "{\"serialNumber\": \"%s\",\"playerName\": \"%s\",\"type\": %s,\"amount\": %s,\"createTime\": \"%s\"}";
            data = String.format(data, transferModel.getSerialNumber(), transferModel.getUserName(), transferModel.getType(), transferModel.getAmount(), formatDate(transferModel.getCreateTime()));
            data = AesUtil.aesEncrypt32(data, key);
            map.put(VrConstants.RequestConstants.VR_DATA, data);
            String result = okHttpService.postJson(okHttpService.proxyClient, url, map);
            result = AesUtil.aesDecrypt32(result, key);
            return JSON.parseObject(result, Map.class);
        } catch (Exception e) {
            log.info("转账出现异常！");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间日期格式化
     */
    public String formatDate(Date date) {
        String dateStr = vrDateFormat.format(date);
        return dateStr.substring(0, dateStr.lastIndexOf(".")) + "Z";
    }

    private CheckTransferResultModel checkTransferRequest(VrParameterModel.CheckTransferParamModel checkTransferParamModel) {
        try {
            String url = checkTransferParamModel.getApi().getSecureCodes().get(checkTransferParamModel.getUrl());
            String key = checkTransferParamModel.getApi().getMd5Key();
            Map<String, Object> map = new HashMap();
            map.put(VrConstants.RequestConstants.VR_VERSION, checkTransferParamModel.getApi().getProxyFore());
            map.put(VrConstants.RequestConstants.VR_ID, checkTransferParamModel.getApi().getAgyAcc());
            String data = "{\"startTime\": \"%s\",\"endTime\": \"%s\",\"serialNumber\": \"%s\",\"playerName \": \"\",\"recordCountPerPage\": %s,\"recordPage\": %s}";
            data = String.format(data, formatDate(checkTransferParamModel.getStartTime()), formatDate(checkTransferParamModel.getEndTime()), checkTransferParamModel.getSerialNumber(), checkTransferParamModel.getRecordCountPerPage(), checkTransferParamModel.getRecordPage());
            data = AesUtil.aesEncrypt32(data, key);
            map.put(VrConstants.RequestConstants.VR_DATA, data);
            String result = okHttpService.postJson(okHttpService.proxyClient, url, map);
            result = AesUtil.aesDecrypt32(result, key);
            return JSON.parseObject(result, CheckTransferResultModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private VrParameterModel.TransferParamModel getTransferParamModel(TransferModel transfer, String url, Integer type) {
        VrParameterModel.TransferParamModel transferModel = new VrParameterModel.TransferParamModel();
        transferModel.setUserName(getUserName(transfer));
        transferModel.setAmount(transfer.getAmount());
        transferModel.setSerialNumber(transfer.getOrderNo());
        transferModel.setCreateTime(new Date());
        transferModel.setType(type);
        transferModel.setApi(transfer.getTGmApi());
        transferModel.setUrl(url);
        return transferModel;
    }


    private Date getTransferStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//date 换成已经已知的Date对象
        cal.add(Calendar.HOUR_OF_DAY, -24);// before 24 hour
        return cal.getTime();
    }

    private String getUserName(UnityParameterModel unityParameterModel){
        return unityParameterModel.getTGmApi().getPrefix().concat(unityParameterModel.getUserName());
    }
}
