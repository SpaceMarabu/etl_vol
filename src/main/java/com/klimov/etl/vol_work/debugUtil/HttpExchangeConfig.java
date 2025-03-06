package com.klimov.etl.vol_work.debugUtil;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HttpExchangeConfig {

    @Bean
    public HttpExchangeRepository httpExchangeRepository() {
        // Хранит недавние запросы/ответы в памяти
        return new InMemoryHttpExchangeRepository();
    }
}
