package com.mci.ticketpilot;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@PropertySource("classpath:ticketpilot.properties")
@Theme(value = "ticketpilot-dark", variant= Lumo.DARK)
@Import(AppConfig.class)
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "ticketpilot");
        SpringApplication.run(Application.class, args);
    }

}
