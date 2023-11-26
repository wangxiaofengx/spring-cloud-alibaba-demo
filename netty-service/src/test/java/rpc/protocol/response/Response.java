package rpc.protocol.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Response {

    Head head;

    Body body;
}
