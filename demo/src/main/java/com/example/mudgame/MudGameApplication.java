package com.example.mudgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.mudgame.model")
@EnableJpaRepositories(basePackages = "com.example.mudgame.repository")
public class MudGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(MudGameApplication.class, args);
    }

}
