package com.mci.ticketpilot.views;

import com.mci.ticketpilot.security.SecurityService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.lists.HelpView;
import com.mci.ticketpilot.views.lists.ProjectListView;
import com.mci.ticketpilot.views.lists.TicketListView;
import com.mci.ticketpilot.views.lists.UserListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private void createHeaderAndDrawer() {

        //logger.info("Building header");

        H1 logo = new H1("Ticket Pilot | goes brrrrr");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        Image logoImage = new Image("images/Unbenannt.jpg ","Company Logo");
        logoImage.setHeight("100px"); // adjust size as needed

        HorizontalLayout header;
        if (securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Logout", click ->
                    securityService.logout());
            header = new HorizontalLayout(logo,logoImage, logout);
            header.addClassName("header-class");
        } else {
            header = new HorizontalLayout(logo, logoImage);
            header.addClassName("header-class");
        }

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo); // <4>
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

        HorizontalLayout linksLayout = new HorizontalLayout();
        linksLayout.add(dashboard, projectlist, ticketlist); // helpList removed from here
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

        FlexLayout drawer = new FlexLayout(); // Change to FlexLayout
        drawer.addClassName("drawer-class");
        drawer.add(linksLayout, helpLayout); // Add both layouts to the drawer
        drawer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Distribute space between layouts
        drawer.setWidthFull();

        VerticalLayout navbar = new VerticalLayout(header, drawer);
        navbar.setWidthFull();

        addToNavbar(navbar);
    }
}
