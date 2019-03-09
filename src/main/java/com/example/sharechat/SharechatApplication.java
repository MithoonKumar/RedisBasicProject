package com.example.sharechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SharechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharechatApplication.class, args);
    }

}
