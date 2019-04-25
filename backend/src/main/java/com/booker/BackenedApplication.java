package com.booker;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.booker.configurationproperties.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class BackenedApplication {

    public static void main(String[] args) {
	SpringApplication.run(BackenedApplication.class, args);
    }

    @Bean
    @Qualifier("passwordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }
}
