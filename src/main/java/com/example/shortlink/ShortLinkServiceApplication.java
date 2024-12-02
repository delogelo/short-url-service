// ShortLinkServiceApplication.java
package com.example.shortlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShortLinkServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortLinkServiceApplication.class, args);
    }
}