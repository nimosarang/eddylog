package com.eddylog.api.config;

import java.util.Base64;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "eddy")
public class AppConfig {

    private byte[] jwtKey; //스프링이 올라가면 키값이 주입된다

    public void setJwtKey(String jwtKey) {
       this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }

    public byte[] getJwtKey() {
        return jwtKey;
    }
}
