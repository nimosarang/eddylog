package com.eddylog.api;

import com.eddylog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class EddylogApplication {

    public static void main(String[] args) {
        SpringApplication.run(EddylogApplication.class, args);
    }

}
