package com.mci.ticketpilot.views;

import com.vaadin.flow.component.UI;
import com.vaadin.testbench.unit.UIUnitTest;
import org.junit.jupiter.api.*;


public class LoginViewTest extends UIUnitTest {

    @Test
    public void loginViewShouldHaveLoginForm() {
        UI ui = new UI();
        LoginView loginView = new LoginView();
        ui.add(loginView);
        Assertions.assertEquals(2, loginView.getChildren().count());
    }
}