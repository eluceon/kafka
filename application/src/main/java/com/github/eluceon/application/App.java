package com.github.eluceon.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.eluceon")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
