package com.mci.ticketpilot.views;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.mci.ticketpilot.security.SecurityService;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainLayoutTest {

    public SecurityService securityService;


    @Before
    public void setup() {
        MockVaadin.setup();
        // Create a mock instance of the SecurityService class
        securityService = Mockito.mock(SecurityService.class);
    }

    @Test
    public void testHeaderAndDrawerCreation() {
        // Create a new instance of the MainLayout class
        MainLayout mainLayout = new MainLayout(securityService);

        // Find the logo image
        Image logoImage = _get(mainLayout, Image.class);
        assertNotNull(logoImage);
        assertEquals("images/Unbenannt.jpg", logoImage.getSrc());

        // Find the header layout
        HorizontalLayout headerLayout = _get(mainLayout, HorizontalLayout.class);
        assertNotNull(headerLayout);

        // Find the logo text
        H1 logoText = _get(headerLayout, H1.class);
        assertNotNull(logoText);
        assertEquals("Ticket Pilot | goes brrrrr", logoText.getText());

        // Find the theme toggle button
        ToggleButton themeToggle = _get(headerLayout, ToggleButton.class);
        assertNotNull(themeToggle);
    }
    @After
    public void tearDown() {
        MockVaadin.tearDown();
    }
}
