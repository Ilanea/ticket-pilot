package com.mci.ticketpilot;

import com.vaadin.flow.component.login.testbench.LoginFormElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Attribute(name = "class", contains = "login-view")
public class LoginViewTest extends VerticalLayoutElement {

    public boolean login(String username, String password) {
        LoginFormElement form = $(LoginFormElement.class).first();
        form.getUsernameField().setValue(username);
        form.getPasswordField().setValue(password);
        form.getSubmitButton().click();

        // Return true if we end up on another page
        try {
            getDriver().manage().timeouts().implicitlyWait(Duration.of(1, ChronoUnit.SECONDS));
            getDriver().findElement(By.tagName("vaadin-app-layout"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}