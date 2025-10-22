package ru.Vladimir.NauJava.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppPropertiesConfig {

    @Value("${app.name:NauJava}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @PostConstruct
    public void printAppInfo() {
        System.out.println("Application: " + appName );
        System.out.println("Version: " + appVersion);
    }
}