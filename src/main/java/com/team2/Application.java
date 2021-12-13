package com.team2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages="com.team2.user_management")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
