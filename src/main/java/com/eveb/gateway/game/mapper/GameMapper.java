package com.eveb.gateway.game.mapper;

import com.eveb.gateway.game.model.TGmGame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GameMapper {

    TGmGame getGmGameOne(@Param("depotName")String depotName, @Param("gameCode")String gameCode);
}
