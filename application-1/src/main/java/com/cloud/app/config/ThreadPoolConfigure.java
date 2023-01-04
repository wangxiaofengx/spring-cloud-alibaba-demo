package com.cloud.app.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.concurrent.ExecutorService;

@Configuration
public class ThreadPoolConfigure {

    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean() {
        ThreadPoolExecutorFactoryBean result = new ThreadPoolExecutorFactoryBean();
        result.setThreadNamePrefix("AppThreadPool-");
        result.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        result.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        return result;
    }

    @Bean
    @ConditionalOnMissingBean
    public ExecutorService executorService(ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean){
        return threadPoolExecutorFactoryBean.getObject();
    }
}
