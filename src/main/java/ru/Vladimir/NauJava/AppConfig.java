package ru.Vladimir.NauJava;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = {"ru.Vladimir.NauJava"})
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @PostConstruct
    public void init() {
        logger.info("Инициализация конфигурации приложения");
        logger.info("Название приложения: {}", appName);
        logger.info("Версия приложения: {}", appVersion);
    }
}
