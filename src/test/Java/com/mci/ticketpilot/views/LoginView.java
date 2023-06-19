package com.mci.ticketpilot.views;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class LoginViewTest {

    private LoginView loginView;

    @BeforeEach
    public void setUp() {
        loginView = new LoginView();
    }

    @Test
    public void beforeEnter_withErrorParameter_setLoginError() {
        // Arrange
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("error", "");

        Location location = Mockito.mock(Location.class);
        when(location.getQueryParameters()).thenReturn((HashMap<String, String>) parameterMap);

        BeforeEnterEvent event = Mockito.mock(BeforeEnterEvent.class);
        when(event.getLocation()).thenReturn(location);

        // Act
        loginView.beforeEnter(event);

        // Assert
        assertTrue(loginView.login.isError());
    }

    @Test
    public void beforeEnter_withoutErrorParameter_loginErrorNotSet() {
        // Arrange
        Map<String, String> parameterMap = new HashMap<>();

        Location location = Mockito.mock(Location.class);
        when(location.getQueryParameters()).thenReturn(parameterMap);

        BeforeEnterEvent event = Mockito.mock(BeforeEnterEvent.class);
        when(event.getLocation()).thenReturn(location);

        // Act
        loginView.beforeEnter(event);

        // Assert
        assertFalse(loginView.login.isError());
    }

    @Test
    public void beforeEnter_withNullLocation_loginErrorNotSet() {
        // Arrange
        BeforeEnterEvent event = Mockito.mock(BeforeEnterEvent.class);
        when(event.getLocation()).thenReturn(null);

        // Act
        loginView.beforeEnter(event);

        // Assert
        assertFalse(loginView.login.isError());
    }
}
