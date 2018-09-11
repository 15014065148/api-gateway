package com.eveb.gateway.game.service;

import com.eveb.gateway.config.CacheDuration;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.mapper.GameMapper;
import com.eveb.gateway.game.model.TGmGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameMapper gameMapper;

    /**
     * TOKEN 查询 半小时后失效
     */
//    @Cacheable(cacheNames = ApplicationConstants.REIDS_DEPOT_TOKEN_KEY, key = "#depotName+'_'+#gameCode")
//    @CacheDuration(duration = 3600)
    public TGmGame queryGame(String depotName,String gameCode) {
        return gameMapper.getGmGameOne(depotName,gameCode);
    }
}
