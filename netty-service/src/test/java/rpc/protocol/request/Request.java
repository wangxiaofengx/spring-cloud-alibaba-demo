package rpc.protocol.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Request {

    Head head;

    Body body;
}
