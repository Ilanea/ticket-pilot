package com.mci.ticketpilot.views;

import com.mci.ticketpilot.security.SecurityService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.calendar.CalendarView;
import com.mci.ticketpilot.views.lists.*;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.html.Image;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;


/**
 * The main view is a top-level placeholder for other views.
 */

public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    public MainLayout(@Autowired SecurityService securityService) {
        this.securityService = securityService;
        createHeaderAndDrawer();
    }

    private void setTheme(boolean dark) {
        var js = "document.documentElement.setAttribute('theme', $0)";

        getElement().executeJs(js, dark ? Lumo.DARK : Lumo.LIGHT);
    }

    private void createHeaderAndDrawer() {

        //logger.info("Building header");

        H1 logo = new H1("Ticket Pilot | goes brrrrr");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        Image logoImage = new Image( "images/Unbenannt.jpg", "Company Logo");


        logoImage.setHeight("40px");
        logoImage.setWidth("40px");



        HorizontalLayout header;

        ToggleButton themeToggle = new ToggleButton("Dark Mode");
        themeToggle.addClassName("theme-toggle");
        themeToggle.addValueChangeListener(evt -> setTheme(evt.getValue()));


        if (securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Logout", click -> securityService.logout());
            Paragraph smallText = new Paragraph("logged in as " + SecurityUtils.getLoggedInUser().getFirstName() + " " + SecurityUtils.getLoggedInUser().getLastName());
            smallText.getStyle().set("font-size", "0.8em");

            VerticalLayout logoutLayout = new VerticalLayout(logout, smallText);
            logoutLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            logoutLayout.setPadding(false);
            logoutLayout.setSpacing(false);

            // Neue horizontalLayout Instanz für themeToggle und logoutLayout
            HorizontalLayout rightLayout = new HorizontalLayout(themeToggle, logoutLayout);


            // Füge rightLayout zum Header hinzu, anstatt themeToggle und logoutLayout direkt hinzuzufügen
            header = new HorizontalLayout(logoImage, logo, rightLayout);
            header.addClassName("header-class");
            header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Platz zwischen den Komponenten verteilen
            header.expand(logo);
        } else {
            header = new HorizontalLayout(logoImage, logo, themeToggle);
            header.addClassName("header-class");
            header.expand(logo);
        }

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();

        RouterLink dashboard = new RouterLink("Dashboard", DashboardView.class);
        dashboard.addClassName("router-link");
        dashboard.setHighlightCondition(HighlightConditions.sameLocation());




        RouterLink userlist = new RouterLink("Users", UserListView.class);
        userlist.addClassName("router-link");
        userlist.setHighlightCondition(HighlightConditions.sameLocation());




        RouterLink projectlist = new RouterLink("Projects", ProjectListView.class);
        projectlist.addClassName("router-link");
        projectlist.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink ticketlist = new RouterLink("Tickets", TicketListView.class);
        ticketlist.addClassName("router-link");
        ticketlist.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink helpList = new RouterLink("Help", HelpView.class);
        helpList.addClassName("router-link");
        helpList.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink kanbanList = new RouterLink("Kanban", KanbanView.class);
        kanbanList.addClassName("router-link");
        kanbanList.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink calendarView = new RouterLink("Calendar", CalendarView.class);
        calendarView.addClassName("router-link");
        calendarView.setHighlightCondition(HighlightConditions.sameLocation());

        HorizontalLayout linksLayout = new HorizontalLayout();
        linksLayout.add(dashboard, projectlist, ticketlist, kanbanList, calendarView); // helpList removed from here
        linksLayout.setWidthFull();
        linksLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        linksLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        // add Userlist only if ADMIN or MANAGER
        if(SecurityUtils.userHasAdminRole() || SecurityUtils.userHasManagerRole()){
            linksLayout.add(userlist);
        }

        HorizontalLayout helpLayout = new HorizontalLayout();
        helpLayout.add(helpList);
        helpLayout.setWidthFull();
        helpLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        helpLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        FlexLayout drawer = new FlexLayout();
        drawer.addClassName("drawer-class");
        drawer.add(linksLayout, helpLayout);
        drawer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        drawer.setWidthFull();

        VerticalLayout navbar = new VerticalLayout(header, drawer);
        navbar.setWidthFull();

        addToNavbar(navbar);
    }
}
