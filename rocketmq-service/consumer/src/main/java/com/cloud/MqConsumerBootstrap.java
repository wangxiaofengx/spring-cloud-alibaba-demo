package com.cloud;

import com.cloud.listener.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication(scanBasePackages={"com.cloud"})
@EnableBinding(MySink.class)
public class MqConsumerBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(MqConsumerBootstrap.class, args);
    }

}
