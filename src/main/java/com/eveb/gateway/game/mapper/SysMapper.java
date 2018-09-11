package com.eveb.gateway.game.mapper;

import com.eveb.gateway.game.model.TGmApi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Miracle
        * @Description:
        * @Date: 16:20 2017/12/14
        **/
@Mapper
public interface SysMapper {

    TGmApi getGmApiOne(TGmApi api);

    List<TGmApi> getGmApiList(String depotName);

    List<String> selectProxys(@Param("groups") String groups);

    List<String> getApiPrefixBySiteCode(@Param("siteCode") String siteCode);

    List<String> getSiteCodeList(@Param("siteCode") String siteCode, @Param("isApi") Integer isApi);

    TGmApi getApiBySiteCode(@Param("siteCode")String siteCode,@Param("depotName")String depotName);
}
