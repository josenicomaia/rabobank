package com.rabobank.trips.messaging;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {
    static final String exchangeName = "exchange";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }
}
