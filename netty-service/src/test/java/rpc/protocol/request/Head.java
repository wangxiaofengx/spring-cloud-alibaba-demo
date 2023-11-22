package rpc.protocol.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class Head implements Serializable {

    private Long id = 0l;

    private Integer bodyLength = 0;
}
