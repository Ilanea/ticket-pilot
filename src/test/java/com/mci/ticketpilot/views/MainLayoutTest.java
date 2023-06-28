package com.mci.ticketpilot.views;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.mci.ticketpilot.security.SecurityService;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import org.junit.jupiter.api.*;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;

public class MainLayoutTest {

    public SecurityService securityService;

    @Test
    public void testHeaderAndDrawerCreation() {
        // Create a new instance of the MainLayout class
        MainLayout mainLayout = new MainLayout(securityService);

        // Find the logo image
        Image logoImage = _get(mainLayout, Image.class);
        Assertions.assertNotNull(logoImage);
        Assertions.assertEquals("images/Unbenannt.jpg", logoImage.getSrc());

        // Find the header layout
        HorizontalLayout headerLayout = _get(mainLayout, HorizontalLayout.class);
        Assertions.assertNotNull(headerLayout);

        // Find the logo text
        H1 logoText = _get(headerLayout, H1.class);
        Assertions.assertNotNull(logoText);
        Assertions.assertEquals("Ticket Pilot | goes brrrrr", logoText.getText());

        // Find the theme toggle button
        ToggleButton themeToggle = _get(headerLayout, ToggleButton.class);
        Assertions.assertNotNull(themeToggle);
    }

}
