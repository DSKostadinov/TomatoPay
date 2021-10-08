package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
public class ThreadPoolConfig {

    @Bean
    public TaskExecutor executor(@Value("${spring.executor.corePoolSize}") int corePoolSize,
                                 @Value("${spring.executor.maxPoolSize}") int maxPoolSize,
                                 @Value("${spring.executor.queueCapacity}") int queueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);

        return executor;
    }
}
