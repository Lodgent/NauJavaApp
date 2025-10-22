package ru.Vladimir.NauJava.Config;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import ru.Vladimir.NauJava.Models.FileEntity;

import java.util.ArrayList;
import java.util.List;



import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Configuration;



@Configuration
@EnableTransactionManagement
public class AppConfig {

    @ComponentScan(basePackages = "ru.Vladimir.NauJava")
    @EnableJpaRepositories(basePackages = "ru.Vladimir.NauJava.repository")
    public class RepositoryConfig {
    }

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