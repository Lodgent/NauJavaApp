package ru.Vladimir.NauJava.Config;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

import ru.Vladimir.NauJava.Models.FileEntity;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.Vladimir.NauJava.dao")
public class AppConfig {

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

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