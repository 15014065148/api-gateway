package com.eveb.gateway.game.service;

import com.eveb.gateway.config.CacheDuration;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.mapper.SysMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysService {

    @Autowired
    private SysMapper sysMapper;

    public TGmApi findGmApiOne(TGmApi api) {
        return sysMapper.getGmApiOne(api);
    }

    @Cacheable(cacheNames=ApplicationConstants.REDIS_GAME_API_CACHE_KEY, key="#depotName")
    public List<TGmApi> findGmApiList(String depotName) {
        return sysMapper.getGmApiList(depotName);
    }

    public List<String> selectProxys(String groups) {
        return sysMapper.selectProxys(groups);
    }

    public List<String> getApiPrefixBySiteCode(String siteCode) {
        return sysMapper.getApiPrefixBySiteCode(siteCode);
    }

    public List<String> getSiteCodeList(String siteCode, Integer isApi) {
        return sysMapper.getSiteCodeList(siteCode, isApi);
    }

    /**
     * 根据平台及代理号获取线路
     * @param depotName
     * @param agy
     * @return
     */
    public TGmApi findTGApiByDepotAndAgy(String depotName,String agy) {
        List<TGmApi> apis = findGmApiList(depotName);
        apis = apis.stream().filter(a -> a.getAgyAcc().equals(agy)).collect(Collectors.toList());
        if (apis.size() == 0) {
            return null;
        }
        return apis.get(0);
    }

    public TGmApi findTGApiByDepotAndSecure(String depotName,String str) {
        List<TGmApi> apis = findGmApiList(depotName);
        apis = apis.stream().filter(a -> a.getSecureCode().contains(str)).collect(Collectors.toList());
        if (apis.size() == 0) {
            return null;
        }
        return apis.get(0);
    }

    /**
     * TOKEN 保存 半小时后失效
     */
    @CachePut(cacheNames = ApplicationConstants.REIDS_DEPOT_TOKEN_KEY, key = "#depotName")
    @CacheDuration(duration = 1800)
    public String updateDepotTokenCache(String depotName,String token) {
        return token;
    }

    /**
     * TOKEN 查询 半小时后失效
     */
    @Cacheable(cacheNames = ApplicationConstants.REIDS_DEPOT_TOKEN_KEY, key = "#depotName")
    @CacheDuration(duration = 1800)
    public String queryDepotTokenCache(String depotName) {
        return "";
    }

    /**
     * 根据网站及代理号获取线路
     * @param siteCode
     * @return
     */
    @Cacheable(cacheNames = ApplicationConstants.REIDS_SITE_API_KEY, key = "#siteCode+'_'+#depotName")
    @CacheDuration(duration = 1800)
    public TGmApi getApiBySiteCode(String siteCode,String depotName) {
        return sysMapper.getApiBySiteCode(siteCode,depotName);
    }
}
