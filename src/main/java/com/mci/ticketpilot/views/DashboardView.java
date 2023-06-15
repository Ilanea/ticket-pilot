package com.mci.ticketpilot.views;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.service.PilotService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.*;

import com.vaadin.flow.component.button.Button;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Ticket Pilot")
public class DashboardView extends VerticalLayout {
    private final PilotService service;
    @Autowired
    public DashboardView(PilotService service) {
        this.service = service;

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(createBoardStats());
    }

    private Component getUserStats() {
        Span stats = new Span(service.countUsers() + " contacts");
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Component getTicketStats() {
        Span stats = new Span(service.countTickets() + " tickets");
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Component getProjectStats() {
        Span stats = new Span(service.countProjects() + " projects");
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Component createBoardStats() {
        Row rootRow = new Row();
        rootRow.add(new H1("Stats"), 1);

        Row nestedRow = new Row(
                getUserStats(),
                getTicketStats(),
                getProjectStats());
        rootRow.addNestedRow(nestedRow);

        Board board = new Board();
        board.add(rootRow);

        return board;
    }
}


