package com.mci.ticketpilot.views;

import com.mci.ticketpilot.security.SecurityService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.lists.ProjectListView;
import com.mci.ticketpilot.views.lists.TicketListView;
import com.mci.ticketpilot.views.lists.UserListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    public MainLayout(@Autowired SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {

        //logger.info("Building header");

        H1 logo = new H1("Ticket Pilot | goes brrrrr");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);


        HorizontalLayout header;
        if (securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Logout", click ->
                    securityService.logout());
            header = new HorizontalLayout(new DrawerToggle(),logo, logout);
        } else {
            header = new HorizontalLayout(logo);
        }


        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }

    public void createDrawer() {

        VerticalLayout drawer = new VerticalLayout();

        RouterLink dashboard = new RouterLink("Dashboard", DashboardView.class);
        RouterLink userlist = new RouterLink("Users", UserListView.class);
        RouterLink projectlist = new RouterLink("Projects", ProjectListView.class);
        RouterLink ticketlist = new RouterLink("Tickets", TicketListView.class);

        drawer.add(dashboard, projectlist, ticketlist);

        // add Userlist only if ADMIN or MANAGER
        if(SecurityUtils.userHasAdminRole() || SecurityUtils.userHasManagerRole()){
            drawer.add(userlist);
        }

        addToDrawer(drawer);

    }
}