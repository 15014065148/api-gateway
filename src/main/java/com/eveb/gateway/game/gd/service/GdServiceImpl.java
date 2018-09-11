package com.eveb.gateway.game.gd.service;

import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.gd.model.GdConstants;
import com.eveb.gateway.game.gd.model.GdParameterModel;
import com.eveb.gateway.game.gd.model.GdResultModel;
import com.eveb.gateway.game.gd.model.GdUtil;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class GdServiceImpl implements DepotService {

    @Autowired
    private GdResultModel gdResultModel;

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
        GdParameterModel gdParameterModel = initParam(playGame);
        gdParameterModel.setAccessKey(playGame.getTGmApi().getMd5Key());
        gdParameterModel.setLang(playGame.getTGmApi().getSecureCodes().get(GdConstants.LANG));
        gdParameterModel.setUrl(playGame.getTGmApi().getSecureCodes().get(GdConstants.LOGINURL));
        gdParameterModel.setOrigin(getOrigin(playGame.getOrigin()));
        return playGame(gdParameterModel);
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        GdParameterModel gdParameterModel = initParam(register);
        gdParameterModel.setMethod(register.getTGmApi().getSecureCodes().get(GdConstants.CREATEMETHOD));
        gdParameterModel.setMessageID(GdConstants.CREATEMESSAGEIDPREFIX + getNowDate() + get5RandomChar());
        return createPlayer(gdParameterModel);
    }

    @Override
    public Object login(LoginModel login) {
        GdParameterModel gdParameterModel = initParam(login);
        gdParameterModel.setAccessKey(login.getTGmApi().getMd5Key());
        gdParameterModel.setLang(login.getTGmApi().getSecureCodes().get(GdConstants.LANG));
        gdParameterModel.setUrl(login.getTGmApi().getSecureCodes().get(GdConstants.LOGINURL));
        return playGame(gdParameterModel);
    }

    @Override
    public Object logout(LoginModel login) {
        GdParameterModel gdParameterModel = initParam(login);
        gdParameterModel.setMethod(login.getTGmApi().getSecureCodes().get(GdConstants.LOGOUTMETHOD));
        gdParameterModel.setMessageID(GdConstants.LOGOUTMESSAGEIDPREFIX + getNowDate() + get5RandomChar());
        return logout(gdParameterModel);
    }

    @Override
    public Object deposit(TransferModel transfer) {
        GdParameterModel gdParameterModel = initParam(transfer);
        gdParameterModel.setMethod(transfer.getTGmApi().getSecureCodes().get(GdConstants.DEPOSITMETHOD));
        if (!checkDepositOrderNo(transfer.getOrderNo())) {
            return new UnityResultModel(false, "存款的orderNo格式为：D+YYMMDDhhmmss+5个随机字符 如：D180816134512K9n8d，请检查");
        }
        gdParameterModel.setMessageID(transfer.getOrderNo());
        gdParameterModel.setAmount(transfer.getAmount().toString());
        return deposit(gdParameterModel);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        GdParameterModel gdParameterModel = initParam(transfer);
        gdParameterModel.setMethod(transfer.getTGmApi().getSecureCodes().get(GdConstants.WITHDRAWALMETHOD));
        if (!checkWithDrawalOrderNo(transfer.getOrderNo())) {
            return new UnityResultModel(false, "取款的orderNo格式为：W+YYMMDDhhmmss+5个随机字符 如：W110830134512K9n8d，请检查");
        }
        gdParameterModel.setMessageID(transfer.getOrderNo());
        gdParameterModel.setAmount(transfer.getAmount().toString());
        return withdrawal(gdParameterModel);
    }

    @Override
    public Object queryBalance(LoginModel login) {
        GdParameterModel gdParameterModel = initParam(login);
        gdParameterModel.setMethod(login.getTGmApi().getSecureCodes().get(GdConstants.BALANCEMETHOD));
        gdParameterModel.setMessageID(GdConstants.BALANCEMESSAGEIDPREFIX + getNowDate() + get5RandomChar());
        return queryBalance(gdParameterModel);
    }

    @Override
    public Object checkTransfer(TransferModel transfer) {
        GdParameterModel gdParameterModel = initParam(transfer);
        gdParameterModel.setMethod(transfer.getTGmApi().getSecureCodes().get(GdConstants.CHECKTRANSFER));
        gdParameterModel.setMessageID(transfer.getOrderNo());
        return checkTransfer(gdParameterModel);
    }

    @Override
    public Object getToken() {
        return null;
    }

    /**
     * 创建会员
     */
    private UnityResultModel createPlayer(GdParameterModel gdParameterModel) {
        String resultXml = createPlayerRequest(gdParameterModel);
        return gdResultModel.getCreatePlayerResult(resultXml);
    }

    /**
     * 会员登出
     */
    private UnityResultModel logout(GdParameterModel gdParameterModel) {
        String resultXml = logoutRequest(gdParameterModel);
        return gdResultModel.getLogoutResult(resultXml);
    }

    /**
     * 会员存款
     */
    private UnityResultModel deposit(GdParameterModel gdParameterModel) {
        String resultXml = depositRequest(gdParameterModel);
        return gdResultModel.getDepositResult(resultXml);
    }

    /**
     * 会员取款
     */
    private UnityResultModel withdrawal(GdParameterModel gdParameterModel) {
        String resultXml = withdrawalRequest(gdParameterModel);
        return gdResultModel.getWithdrawalResult(resultXml);
    }

    /**
     * 查询余额
     */
    private UnityResultModel queryBalance(GdParameterModel gdParameterModel) {
        String resultXml = queryBalanceRequest(gdParameterModel);
        return gdResultModel.getQueryBalanceResult(resultXml);
    }

    /**
     * 确认转账
     */
    private UnityResultModel checkTransfer(GdParameterModel gdParameterModel) {
        String resultXml = checkTransferRequest(gdParameterModel);
        return gdResultModel.getCheckTransferResult(resultXml);
    }

    /**
     * 会员登陆，玩游戏
     */
    private UnityResultModel playGame(GdParameterModel gdParameterModel) {
        if (StringUtils.isEmpty(gdParameterModel.getOrigin())) {
            gdParameterModel.setOrigin("0");
        }
        String resultXml = playGameRequest(gdParameterModel);
        return gdResultModel.getPlayGameResult(resultXml);
    }


    private String createPlayerRequest(GdParameterModel gdParameterModel) {
        String requestParam = String.format(gdParameterModel.getCreatePlayerParamStr(), gdParameterModel.getMethod(), gdParameterModel.getMerchantID(), gdParameterModel.getMessageID(), gdParameterModel.getUserID(), gdParameterModel.getCurrencyCode());
        log.info("\n" + "GD平台====>创建会员请求：" + "\n" + "请求URL: " + gdParameterModel.getUrl() + "\n" + "请求参数: " + requestParam);
        return okHttpService.postXml(okHttpService.proxyClient, gdParameterModel.getUrl(), requestParam);
    }

    private String logoutRequest(GdParameterModel gdParameterModel) {
        String requestParam = String.format(gdParameterModel.getLogoutParamStr(), gdParameterModel.getMethod(), gdParameterModel.getMerchantID(), gdParameterModel.getMessageID(), gdParameterModel.getUserID());
        log.info("\n" + "GD平台====>会员登出请求：" + "\n" + "请求URL: " + gdParameterModel.getUrl() + "\n" + "请求参数: " + requestParam);
        return okHttpService.postXml(okHttpService.proxyClient, gdParameterModel.getUrl(), requestParam);
    }

    private String depositRequest(GdParameterModel gdParameterModel) {
        String requestParam = String.format(gdParameterModel.getDepositParamStr(), gdParameterModel.getMethod(), gdParameterModel.getMerchantID(), gdParameterModel.getMessageID(), gdParameterModel.getUserID(), gdParameterModel.getCurrencyCode(), gdParameterModel.getAmount());
        log.info("\n" + "GD平台====>会员存款请求：" + "\n" + "请求URL: " + gdParameterModel.getUrl() + "\n" + "请求参数: " + requestParam);
        return okHttpService.postXml(okHttpService.proxyClient, gdParameterModel.getUrl(), requestParam);
    }

    private String withdrawalRequest(GdParameterModel gdParameterModel) {
        String requestParam = String.format(gdParameterModel.getWithdrawalParamStr(), gdParameterModel.getMethod(), gdParameterModel.getMerchantID(), gdParameterModel.getMessageID(), gdParameterModel.getUserID(), gdParameterModel.getCurrencyCode(), gdParameterModel.getAmount());
        log.info("\n" + "GD平台====>会员取款请求：" + "\n" + "请求URL: " + gdParameterModel.getUrl() + "\n" + "请求参数: " + requestParam);
        return okHttpService.postXml(okHttpService.proxyClient, gdParameterModel.getUrl(), requestParam);
    }

    private String queryBalanceRequest(GdParameterModel gdParameterModel) {
        String requestParam = String.format(gdParameterModel.getQueryBalanceParamStr(), gdParameterModel.getMethod(), gdParameterModel.getMerchantID(), gdParameterModel.getMessageID(), gdParameterModel.getUserID(), gdParameterModel.getCurrencyCode());
        log.info("\n" + "GD平台====>查询余额请求：" + "\n" + "请求URL: " + gdParameterModel.getUrl() + "\n" + "请求参数: " + requestParam);
        return okHttpService.postXml(okHttpService.proxyClient, gdParameterModel.getUrl(), requestParam);
    }

    private String checkTransferRequest(GdParameterModel gdParameterModel) {
        String requestParam = String.format(gdParameterModel.getCheckTransferParamStr(), gdParameterModel.getMethod(), gdParameterModel.getMerchantID(), GdConstants.CHECKCLIENTMESSAGEIDPREFIX + getNowDate() + get5RandomChar(), gdParameterModel.getMessageID(), gdParameterModel.getUserID(), gdParameterModel.getCurrencyCode());
        log.info("\n" + "GD平台====>确认转账请求：" + "\n" + "请求URL: " + gdParameterModel.getUrl() + "\n" + "请求参数: " + requestParam);
        return okHttpService.postXml(okHttpService.proxyClient, gdParameterModel.getUrl(), requestParam);
    }

    private String playGameRequest(GdParameterModel gdParameterModel) {
        String loginTokenID = get9NumberString();
        String Key = GdUtil.String2SHA256StrJava(gdParameterModel.getMerchantID() + loginTokenID + gdParameterModel.getAccessKey() + gdParameterModel.getUserID() + gdParameterModel.getCurrencyCode());
        String requestParam = String.format(gdParameterModel.getLoginParamStr(), gdParameterModel.getUrl(), loginTokenID, gdParameterModel.getMerchantID(), gdParameterModel.getLang(), gdParameterModel.getUserID(), gdParameterModel.getCurrencyCode(), Key, gdParameterModel.getOrigin());
        log.info("\n" + "GD平台====>玩游戏/会员登陆请求：" + "\n" + "请求URL: " + requestParam);
        return requestParam;
    }


    ;

    /**
     * 随机生成5个字符
     */
    private String get5RandomChar() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

    /**
     * 获取当前时间并格式化
     */
    private String getNowDate() {
        return DateUtil.formatDate(new Date(), GdConstants.DATEFORMAT);
    }

    /**
     * 统一初始化参数封装
     */
    private GdParameterModel initParam(UnityParameterModel unityParameterModel) {
        GdParameterModel gdParameterModel = new GdParameterModel();
        gdParameterModel.setUrl(unityParameterModel.getTGmApi().getSecureCodes().get(GdConstants.COMMONURL));
        gdParameterModel.setMerchantID(unityParameterModel.getTGmApi().getSecureCodes().get(GdConstants.MERCHANTID));
        gdParameterModel.setUserID(unityParameterModel.getTGmApi().getPrefix() + unityParameterModel.getUserName());
        gdParameterModel.setCurrencyCode(unityParameterModel.getTGmApi().getSecureCodes().get(GdConstants.CURRENCYCODE));
        return gdParameterModel;
    }

    /**
     * 随机生成9个数字字符
     */
    private String get9NumberString() {
        return RandomStringUtils.randomAlphabetic(9);
    }

    /**
     * 获取终端
     * 设备类型：PC、H5、APP
     */
    private String getOrigin(String Origin) {
        if (StringUtils.isEmpty(Origin)) {
            return "0";
        }
        switch (Origin) {
            case ApplicationConstants
                    .ORIGIN_PC:
                return "0";
            case ApplicationConstants
                    .ORIGIN_APP:
                return "1";
            case ApplicationConstants
                    .ORIGIN_H5:
                return "0";
        }
        return "0";
    }

    /**
     * 存款orderNo校验
     */
    private Boolean checkDepositOrderNo(String orderNo) {
        String regex = "^[D][0-9]{12}[\\w]{5}$";
        return orderNo.matches(regex);
    }

    /**
     * 取款orderNo校验
     */
    private Boolean checkWithDrawalOrderNo(String orderNo) {
        String regex = "^[W][0-9]{12}[\\w]{5}$";
        return orderNo.matches(regex);

    }
}
