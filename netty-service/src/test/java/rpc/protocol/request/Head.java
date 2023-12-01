package rpc.protocol.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Head implements Serializable {

    private Long id;

    private Integer bodyLength;
}
