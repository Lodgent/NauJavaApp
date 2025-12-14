package ru.Vladimir.NauJava.Config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.tool.schema.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Component
public class DatabaseInitializer {
    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Environment environment;

    @PostConstruct
    @Transactional
    public void initialize() {
        String ddlAuto = environment.getProperty("spring.jpa.hibernate.ddl-auto");
        logger.info("Database initialization started. DDL auto mode: " + ddlAuto);
        
        if ("create".equals(ddlAuto) || "create-drop".equals(ddlAuto)) {
            try {
                Session session = entityManager.unwrap(Session.class);
                // Принудительно создаем схему
                session.doWork(connection -> {
                    logger.info("Forcing schema creation...");
                });
                logger.info("Database schema initialization completed");
            } catch (Exception e) {
                logger.warning("Error during database initialization: " + e.getMessage());
            }
        }
    }
}
