package com.eveb.gateway.game.cq9.mapper;

import com.eveb.gateway.game.cq9.model.MbrTransferRecordModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MbrTransferRecordMapper {

     MbrTransferRecordModel getTransferRecord(@Param("mtcode") String mtcode);

     int insertTransferRecord(@Param("rd") MbrTransferRecordModel rd);

    int updateTransferRecord(@Param("rd") MbrTransferRecordModel rd);
}
