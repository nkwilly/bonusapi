package com.systemedebons.bonification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.systemedebons.bonification.Repository")
@ComponentScan({"com.systemedebons.bonification", "com.systemedebons.bonification.Security.Service"})
public class BonificationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BonificationApiApplication.class, args);
    }




}
