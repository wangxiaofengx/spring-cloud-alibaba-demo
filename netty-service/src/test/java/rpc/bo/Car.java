package rpc.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Car implements Serializable {

    private Long id;
    private String name;
}
