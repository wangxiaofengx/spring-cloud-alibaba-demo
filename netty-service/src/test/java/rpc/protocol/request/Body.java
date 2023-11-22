package rpc.protocol.request;

import lombok.Data;

@Data
public class Body {

    String name;

    String method;

    Class<?>[] parameterTypes;

    Object[] args;
}
