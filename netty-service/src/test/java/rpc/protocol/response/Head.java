package rpc.protocol.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Head implements Serializable {

    private Long id;

    private Integer length;
}
