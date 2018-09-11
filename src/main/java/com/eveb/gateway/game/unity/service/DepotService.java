package com.eveb.gateway.game.unity.service;

import com.eveb.gateway.game.unity.model.LoginModel;
import com.eveb.gateway.game.unity.model.PlayGameModel;
import com.eveb.gateway.game.unity.model.RegisterModel;
import com.eveb.gateway.game.unity.model.TransferModel;


public interface DepotService {

    /***
     * 试玩游戏
     * @param playGame
     * @return
     */
    Object tryPlayGame(PlayGameModel playGame);

    /***
     * 正式玩游戏
     * @param playGame
     * @return
     */
    Object playGame(PlayGameModel playGame);

    /***
     * 打开大厅
     * @param playGame
     * @return
     */
    Object openHall(PlayGameModel playGame);

    /***
     * 创建玩家
     * @param register
     * @return
     */
    Object createPlayer(RegisterModel register);

    /***
     * 登陆
     * @param login
     * @return
     */
    Object login(LoginModel login);

    /***
     * 登出
     * @param login
     * @return
     */
    Object logout(LoginModel login);

    /***
     * 转入的第三方
     * @param transfer
     * @return
     */
    Object deposit(TransferModel transfer);

    /***
     * 从第三方转出
     * @param transfer
     * @return
     */
    Object withdrawal(TransferModel transfer);

    /***
     * 查询余额
     * @param login
     * @return
     */
    Object queryBalance(LoginModel login);

    /***
     * 确认转账
     * @param transfer
     * @return
     */
    Object checkTransfer(TransferModel transfer);

    /***
     * 登陆
     * @return
     */
    Object getToken();
}
