package com.cloud.stock.mapper;

import com.cloud.stock.dto.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("select * from order_info")
    List<OrderInfo> findAll();

    @Insert("insert into order_info(name,status) values(#{name}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Long create(OrderInfo orderInfo);
}
