package com.mci.ticketpilot.views;

import com.vaadin.flow.component.UI;
import com.vaadin.testbench.TestBenchTestCase;
import org.junit.Assert;
import org.junit.Test;

public class LoginViewTest extends TestBenchTestCase {

    @Test
    public void loginViewShouldHaveLoginForm() {
        UI ui = new UI();
        LoginView loginView = new LoginView();
        ui.add(loginView);
        Assert.assertEquals(2, loginView.getChildren().count());  //H1 and LoginForm
    }
}