package com.cloud.message;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Demo01Message {

    /**
     * 编号
     */
    private Integer id;
}