package com.eveb.gateway.game.unity.controller;

import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/unity/")
public class UnityController {
    /**
     * 试玩游戏
     */
    @RequestMapping("*/tryPlayGame")
    public Object tryPlayGame(@RequestBody PlayGameModel playGame, DepotService depotService) {
        return depotService.tryPlayGame(playGame);
    }

    /**
     * 玩游戏
     */
    @RequestMapping("*/playGame")
    public Object playGame(@RequestBody PlayGameModel playGame, DepotService depotService) {
        return depotService.playGame(playGame);
    }

    /**
     * 打开大厅
     */
    @RequestMapping("*/openHall")
    public Object openHall(@RequestBody PlayGameModel playGame, DepotService depotService) {
        return depotService.openHall(playGame);
    }

    /**
     * 登陆
     */
    @RequestMapping("*/login")
    public Object login(@RequestBody LoginModel login, DepotService depotService) {
        return depotService.login(login);
    }

    /**
     * 注销
     */
    @RequestMapping("*/logout")
    public Object logout(@RequestBody LoginModel login, DepotService depotService) {
        return depotService.logout(login);
    }

    /**
     * 创建会员
     */

    @RequestMapping("*/createMember")
    public Object createMember(@RequestBody RegisterModel register, DepotService depotService) {
        return depotService.createPlayer(register);
    }

    /**
     * 存款
     */
    @RequestMapping("*/deposit")
    public Object deposit(@RequestBody TransferModel transfer, DepotService depotService) {
        if (StringUtils.isEmpty(transfer.getUserName())) {
            return new UnityResultModel(false, "用户名不能为空！");
        }
        transfer.setAmount(transfer.getAmount().setScale(2, BigDecimal.ROUND_DOWN));
        return depotService.deposit(transfer);
    }

    /**
     * 取款
     */
    @RequestMapping("*/withdrawal")
    public Object withdrawal(@RequestBody TransferModel transfer, DepotService depotService) {
        if (StringUtils.isEmpty(transfer.getUserName())) {
            return new UnityResultModel(false, "用户名不能为空！");
        }
        transfer.setAmount(transfer.getAmount().setScale(2, BigDecimal.ROUND_DOWN));
        return depotService.withdrawal(transfer);
    }

    /**
     * 查询余额
     */
    @RequestMapping("*/queryBalance")
    public Object queryBalance(@RequestBody LoginModel login, DepotService depotService) {
        return depotService.queryBalance(login);
    }

    /**
     * 确认转账
     */
    @RequestMapping("*/checkTransfer")
    public Object checkTransfer(@RequestBody TransferModel transfer, DepotService depotService) {
        return depotService.checkTransfer(transfer);
    }
}
