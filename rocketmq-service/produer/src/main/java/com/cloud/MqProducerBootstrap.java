package com.cloud;

import com.cloud.message.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication(scanBasePackages={"com.cloud"})
@EnableBinding(MySource.class)
public class MqProducerBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(MqProducerBootstrap.class, args);
    }
}
