package com.klimov.etl.vol_work.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class ServiceConfiguration {

    @Bean(name = "singleThreadExecutor")
    public Executor singleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
