package com.mci.ticketpilot.views;

import com.mci.ticketpilot.security.SecurityService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.calendar.CalendarView;
import com.mci.ticketpilot.views.lists.*;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Text;
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

        ToggleButton themeToggle = new ToggleButton("Dark Mode:");
        themeToggle.setSizeFull();
        themeToggle.addClassName("theme-toggle");
        themeToggle.addValueChangeListener(evt -> setTheme(evt.getValue()));


        if (securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Logout: " +  SecurityUtils.getLoggedInUser().toString(), click -> securityService.logout());

            // Neue horizontalLayout Instanz für themeToggle und logoutLayout
            HorizontalLayout rightLayout = new HorizontalLayout(themeToggle, logout);

            // Füge rightLayout zum Header hinzu, anstatt themeToggle und logoutLayout direkt hinzuzufügen
            header = new HorizontalLayout(logoImage, logo, rightLayout);
            header.addClassName("header-class");

            header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

            header.expand(logo);
        } else {
            header = new HorizontalLayout(logoImage, logo, themeToggle);
            header.addClassName("header-class");
            header.expand(logo);
        }

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();

        Image dashboardIcon = new Image("images/homeicon.png", "Dashboard");
        dashboardIcon.setHeight("15px");
        dashboardIcon.setWidth("15px");

        Image userIcon = new Image("images/usericon.png", "Users");
        userIcon.setHeight("15px");
        userIcon.setWidth("15px");

        Image projectIcon = new Image("images/projecticon.png", "Projects");
        projectIcon.setHeight("15px");
        projectIcon.setWidth("15px");

        Image ticketIcon = new Image("images/ticketicon.png", "Tickets");
        ticketIcon.setHeight("15px");
        ticketIcon.setWidth("15px");

        Image calendarIcon = new Image("images/calendaricon.png", "Calendar");
        calendarIcon.setHeight("15px");
        calendarIcon.setWidth("15px");

        Image helpIcon = new Image("images/helpicon.png", "Help");
        helpIcon.setHeight("15px");
        helpIcon.setWidth("15px");

        Image kanbanIcon = new Image("images/kanbanicon.png", "Kanban");
        kanbanIcon.setHeight("15px");
        kanbanIcon.setWidth("15px");



        VerticalLayout dashboardLayout = new VerticalLayout();
        dashboardLayout.add( new Text("Dashboard"), dashboardIcon);
        dashboardLayout.setSpacing(false);
        dashboardLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        RouterLink dashboard = new RouterLink((String) null, DashboardView.class);
        dashboard.add(dashboardLayout);
        dashboard.addClassName("router-link");
        dashboard.setHighlightCondition(HighlightConditions.sameLocation());


        VerticalLayout userlayout = new VerticalLayout();
        userlayout.add( new Text("Users"), userIcon);
        userlayout.setSpacing(false);
        userlayout.setAlignItems(FlexComponent.Alignment.CENTER);

        RouterLink userlist = new RouterLink((String) null, UserListView.class);
        userlist.add(userlayout);
        userlist.addClassName("router-link");
        userlist.setHighlightCondition(HighlightConditions.sameLocation());

      
        VerticalLayout projectlayout = new VerticalLayout();
        projectlayout.setSpacing(false);
        projectlayout.add( new Text("Projects"), projectIcon);
        projectlayout.setAlignItems(FlexComponent.Alignment.CENTER);

        RouterLink projectlist = new RouterLink((String) null, ProjectListView.class);
        projectlist.add(projectlayout);
        projectlist.addClassName("router-link");
        projectlist.setHighlightCondition(HighlightConditions.sameLocation());


        VerticalLayout ticketlayout = new VerticalLayout();
        ticketlayout.setSpacing(false);
        ticketlayout.add( new Text("Tickets"), ticketIcon);
        ticketlayout.setAlignItems(FlexComponent.Alignment.CENTER);

        RouterLink ticketlist = new RouterLink((String) null, TicketListView.class);
        ticketlist.add(ticketlayout);
        ticketlist.addClassName("router-link");
        ticketlist.setHighlightCondition(HighlightConditions.sameLocation());


        VerticalLayout helpiconLayout = new VerticalLayout();
        helpiconLayout.setSpacing(false);
        helpiconLayout.add( new Text("Help"), helpIcon);
        helpiconLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        RouterLink helpList = new RouterLink((String) null, HelpView.class);
        helpList.add(helpiconLayout);
        helpList.addClassName("router-link");
        helpList.setHighlightCondition(HighlightConditions.sameLocation());


        VerticalLayout kanbanLayout = new VerticalLayout();
        kanbanLayout.add( new Text("Kanban"), kanbanIcon);
        kanbanLayout.setSpacing(false);
        kanbanLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        RouterLink kanbanList = new RouterLink((String) null, KanbanView.class);
        kanbanList.add(kanbanLayout);
        kanbanList.addClassName("router-link");
        kanbanList.setHighlightCondition(HighlightConditions.sameLocation());


        VerticalLayout calendarLayout = new VerticalLayout();
        calendarLayout.setSpacing(false);
        calendarLayout.add( new Text("Calendar"), calendarIcon);
        calendarLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        RouterLink calendarView = new RouterLink((String) null, CalendarView.class);
        calendarView.add(calendarLayout);
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
