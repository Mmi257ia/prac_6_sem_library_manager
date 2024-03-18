package ru.msu.cmc.library_manager;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.msu.cmc.library_manager.model.*;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
public class HibernateDatabaseConfig {
    @Value("${driver}")
    private String DB_DRIVER;
    @Value("${url}")
    private String DB_URL;
    @Value("${user_name}")
    private String DB_USERNAME;
    @Value("${password}")
    private String DB_PASSWORD;

    private SessionFactory sessionFactory;


    private SessionFactory buildSessionFactory() {
        try {
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
            Properties props = new Properties();
            props.setProperty("hibernate.connection.driver_class", DB_DRIVER);
            props.setProperty("hibernate.connection.url", DB_URL);
            props.setProperty("hibernate.connection.username", DB_USERNAME);
            props.setProperty("hibernate.connection.password", DB_PASSWORD);
            props.setProperty("connection_pool_size", "1");
            props.setProperty("hibernate.hbm2ddl.auto", "update");

            configuration.setProperties(props);
            configuration.addAnnotatedClass(Author.class);
            configuration.addAnnotatedClass(Publisher.class);
            configuration.addAnnotatedClass(Product.class);
            configuration.addAnnotatedClass(Book.class);
            configuration.addAnnotatedClass(Reader.class);
            configuration.addAnnotatedClass(Issue.class);

            StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            return configuration.buildSessionFactory(standardServiceRegistry);
        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Bean
    public SessionFactory getSessionFactory() {
        if (sessionFactory == null)
            sessionFactory = buildSessionFactory();
        return sessionFactory;
    }
}
