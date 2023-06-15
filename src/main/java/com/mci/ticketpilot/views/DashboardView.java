package com.mci.ticketpilot.views;

import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.dashboard.DashboardProjects;
import com.mci.ticketpilot.views.dashboard.DashboardTickets;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;


@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Ticket Pilot")
public class DashboardView extends VerticalLayout {
    private final PilotService service;

    private Accordion accordion = new Accordion();
    DashboardProjects dashboardProjects;
    DashboardTickets dashboardTickets;
    HorizontalLayout layout;

    @Autowired
    public DashboardView(PilotService service) {
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        this.service = service;

        // Initialize Grids
        this.dashboardProjects = new DashboardProjects(this.service);
        this.dashboardTickets = new DashboardTickets(this.service);

        // Create accordion
        accordion.open(0);
        accordion.setSizeFull();
        AccordionPanel projects = accordion.add("My Projects", this.dashboardProjects);
        projects.addThemeVariants(DetailsVariant.FILLED);
        AccordionPanel tickets = accordion.add("My Tickets", this.dashboardTickets);
        tickets.addThemeVariants(DetailsVariant.FILLED);

        if(SecurityUtils.userHasManagerRole() || SecurityUtils.userHasAdminRole()) {
            AccordionPanel analytics = accordion.add("Analytics", createBoardAnalytics());
            analytics.addThemeVariants(DetailsVariant.FILLED);
        }

        setAlignItems(FlexComponent.Alignment.BASELINE);

        add(accordion);
    }

    private HorizontalLayout createBoardAnalytics(){
        layout = new HorizontalLayout();
        layout.addClassName("board-analytics");
        layout.add(new H1("Here would be our analytics, but they're not free!"));

        return layout;
    }
}


