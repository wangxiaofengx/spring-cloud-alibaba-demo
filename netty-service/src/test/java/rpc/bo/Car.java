package rpc.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Car implements Serializable {

    private Long id;
    private String name;
}
