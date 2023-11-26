package rpc.protocol.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Body implements Serializable {

    String name;

    String method;

    Class<?>[] parameterTypes;

    Object[] args;
}
