package com.example.sharechat.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;


@Configuration
public class config {

    @Bean
    public Jedis jedisConnectionFactory() {
        Jedis jedis = new Jedis("localhost");
        //jedis.flushAll();
        return jedis;
    }
}