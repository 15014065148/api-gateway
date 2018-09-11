package com.eveb.gateway.game.pt.service;

import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.pt.model.PtConstants;
import com.eveb.gateway.game.pt.model.PtParameterModel;
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

@Slf4j
@Service
public class PtService implements DepotService {
    @Autowired
    public OkHttpService okHttpService;
    @Autowired
    private SysService sysService;
    private static final String DEPOT_NAME = PlatFromEnum.ENUM_PT.getKey();

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
            PtParameterModel ptpara = new PtParameterModel();
            ptpara.setMod(PtConstants.mod.createMember);
            ptpara.setLoginname(api.getPrefix() + register.getUserName());
            ptpara.setAdminname(api.getAgyAcc());
            ptpara.setKioskname(api.getAgyAcc());
            return okHttpService.post(okHttpService.initPtClient(api.getSecureCodes().get(PtConstants.JsonKey.ENTITY_KEY_NAME)), api.getPcUrl() + ptpara.toString(), "");
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
        return transfer(transfer, PtConstants.mod.deposit);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return transfer(transfer, PtConstants.mod.withdraw);
    }

    private Object transfer(TransferModel transfer, String mod) {
        try {
            TGmApi api = sysService.getApiBySiteCode(transfer.getSiteCode(), DEPOT_NAME);
            PtParameterModel ptpara = new PtParameterModel();
            ptpara.setMod(mod);
            ptpara.setLoginname(api.getPrefix() + transfer.getUserName());
            ptpara.setAdminname(api.getAgyAcc());
            ptpara.setAmount(transfer.getAmount().intValue());
            ptpara.setExternaltranid(transfer.getOrderNo());
            return okHttpService.post(okHttpService.initPtClient(api.getSecureCodes().get(PtConstants.JsonKey.ENTITY_KEY_NAME)), api.getPcUrl() + ptpara.toString(), "");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object queryBalance(LoginModel login) {
        try {
            TGmApi api = sysService.getApiBySiteCode(login.getSiteCode(), DEPOT_NAME);
            PtParameterModel ptpara = new PtParameterModel();
            ptpara.setMod(PtConstants.mod.info);
            ptpara.setLoginname(api.getPrefix() + login.getUserName());
            return okHttpService.post(okHttpService.initPtClient(api.getSecureCodes().get(PtConstants.JsonKey.ENTITY_KEY_NAME)), api.getPcUrl() + ptpara.toString(), "");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object checkTransfer(TransferModel transferModel) {
        try {
            TGmApi api = sysService.getApiBySiteCode(transferModel.getSiteCode(), DEPOT_NAME);
            PtParameterModel ptpara = new PtParameterModel();
            ptpara.setMod(PtConstants.mod.checktransaction);
            ptpara.setExternaltranid(transferModel.getOrderNo());
            return okHttpService.post(okHttpService.initPtClient(api.getSecureCodes().get(PtConstants.JsonKey.ENTITY_KEY_NAME)), api.getPcUrl() + ptpara.toString(), "");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object getToken() {
        return null;
    }
}
