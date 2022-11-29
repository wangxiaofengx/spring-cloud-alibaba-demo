package com.cloud.stock.mapper;


import com.cloud.stock.dto.StockInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockMapper {

    @Select("select * from stock_info")
    List<StockInfo> findAll();

    @Insert("insert into stock_info(name,status) values(#{name}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Long create(StockInfo stockInfo);
}
