package ru.Vladimir.NauJava.Config;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import ru.Vladimir.NauJava.Models.FileEntity;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {


    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public List<FileEntity> fileContainer() {
        return new ArrayList<>();
    }


    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public List<FileEntity> linkContainer() {
        return new ArrayList<>();
    }
}