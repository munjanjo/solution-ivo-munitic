package org.example.abysaltobackendtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AbysaltoBackendTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbysaltoBackendTaskApplication.class, args);
    }

}
