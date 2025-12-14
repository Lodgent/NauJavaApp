package ru.Vladimir.NauJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "ru.Vladimir.NauJava.Models")
public class NauJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NauJavaApplication.class, args);
    }
}