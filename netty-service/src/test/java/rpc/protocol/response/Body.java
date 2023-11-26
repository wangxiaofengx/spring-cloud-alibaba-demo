package rpc.protocol.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Body implements Serializable {

    Integer code;

    Object result;
}
