package com.shinhan_hackathon.the_family_guardian.global.config;

import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

@Configuration
public class ExecutorConfig {

    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder = new ThreadPoolTaskExecutorBuilder();
        threadPoolTaskExecutorBuilder
                .corePoolSize(2)
                .maxPoolSize(10)
                .queueCapacity(30)
                .threadNamePrefix("Executor-");
        return threadPoolTaskExecutorBuilder.build();
    }
}
