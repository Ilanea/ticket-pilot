package com.mci.ticketpilot.security;

import com.mci.ticketpilot.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private final PilotUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(PilotUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/images/*.png", "/h2-console").permitAll();

        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService userDetailsServiceBean() {
        return userDetailsService;
    }
}