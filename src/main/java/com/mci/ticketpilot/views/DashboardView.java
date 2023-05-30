package com.mci.ticketpilot.views;

import com.mci.ticketpilot.data.service.TicketService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "", layout = MainLayout.class) // <1>
@PageTitle("Dashboard | Ticket Pilot")
public class DashboardView extends VerticalLayout {
    private final TicketService service;


    public DashboardView(TicketService service) { // <2>
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // <3>

        add(getUserStats(),
                getTicketStats(),
                getProjectStats());
    }

    private Component getUserStats() {
        Span stats = new Span(service.countUsers() + " contacts"); // <4>
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Component getTicketStats() {
        Span stats = new Span(service.countTickets() + " tickets"); // <4>
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Component getProjectStats() {
        Span stats = new Span(service.countProjects() + " projects"); // <4>
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

}