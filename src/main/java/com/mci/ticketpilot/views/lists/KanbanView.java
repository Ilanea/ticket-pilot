package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.TicketStatus;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "kanban", layout = MainLayout.class)
@PageTitle("Kanban | Ticket Pilot")
public class KanbanView extends VerticalLayout {
    private PilotService service;
    private Map<String, VerticalLayout> statusColumns;

    public KanbanView(PilotService service) {
        this.service = service;
        addClassName("kanban-view");
        setSizeFull();
        this.statusColumns = new HashMap<>();

        VerticalLayout colum1 = createStatusColumn("OPEN");
        colum1.addClassName("kanban-column");
        colum1.setWidth("250px");
        colum1.setHeight("1300px");
        colum1.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        VerticalLayout colum2 = createStatusColumn("IN_PROGRESS");
        colum2.addClassName("kanban-column");
        colum2.setWidth("250px");
        colum2.setHeight("1300px");
        colum2.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        VerticalLayout colum3 = createStatusColumn("RESOLVED");
        colum3.addClassName("kanban-column");
        colum3.setWidth("250px");
        colum3.setHeight("1300px");
        colum3.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        VerticalLayout colum4 = createStatusColumn("CLOSED");
        colum4.addClassName("kanban-column");
        colum4.setWidth("250px");
        colum4.setHeight("1300px");
        colum4.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        VerticalLayout colum5 = createStatusColumn("REOPENED");
        colum5.addClassName("kanban-column");
        colum5.setWidth("250px");
        colum5.setHeight("1300px");
        colum5.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        VerticalLayout colum6 = createStatusColumn("ON_HOLD");
        colum6.addClassName("kanban-column");
        colum6.setWidth("250px");
        colum6.setHeight("1300px");
        colum6.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        HorizontalLayout kanbanLayout = new HorizontalLayout(colum1, colum2, colum3, colum4, colum5, colum6);

        statusColumns.put("OPEN", colum1);
        statusColumns.put("IN_PROGRESS", colum2);
        statusColumns.put("RESOLVED", colum3);
        statusColumns.put("CLOSED", colum4);
        statusColumns.put("REOPENED", colum5);
        statusColumns.put("ON_HOLD", colum6);

        add(kanbanLayout);

        updateView();
    }

    private VerticalLayout createStatusColumn(String status) {
        VerticalLayout statusColumn = new VerticalLayout();
        statusColumn.addClassName("status-column");
        statusColumn.add(status); // You'd want to use a Label or similar here
        return statusColumn;
    }

    private void updateView() {
        for (Ticket ticket : service.findAllTickets()) {
            TicketStatus status = ticket.getTicketStatus();
            // Convert the status to a string
            String statusString = status.name();
            VerticalLayout column = statusColumns.get(statusString);

            if (column != null) {
                Component ticketComponent = createTicketComponent(ticket);
                column.add(ticketComponent);
            } else {
                System.out.println("Could not find column for status " + statusString);
            }
        }
    }


    private Component createTicketComponent(Ticket ticket) {
        VerticalLayout ticketDiv = new VerticalLayout();
        ticketDiv.addClassName("ticket");

        Label ticketNameLabel = new Label("Name: " + ticket.getTicketName() + "\n");
        Label ticketDescriptionLabel = new Label("Description: " + ticket.getTicketDescription() + "\n");
        Label ticketPriorityLabel = new Label("Priority: " + ticket.getTicketPriority() + "\n");
        Label ticketAssigneeLabel = new Label("Assignee: " + ticket.getAssignee() + "\n");

        ticketNameLabel.addClassName("ticket-name");
        ticketDescriptionLabel.addClassName("ticket-description");
        switch (ticket.getTicketPriority()) {
            case LOW:
                ticketPriorityLabel.addClassName("ticket-priority-low");
                break;
            case MEDIUM:
                ticketPriorityLabel.addClassName("ticket-priority-medium");
                break;
            case HIGH:
                ticketPriorityLabel.addClassName("ticket-priority-high");
                break;
            case DEFAULT:
                ticketPriorityLabel.addClassName("ticket-priority-default");
                break;
            case NEXT_SPRINT:
                ticketPriorityLabel.addClassName("ticket-priority-next-sprint");
                break;
        }

        ticketAssigneeLabel.addClassName("ticket-assignee");

        ticketDiv.add(ticketNameLabel, ticketDescriptionLabel, ticketPriorityLabel, ticketAssigneeLabel);

        ticketDiv.addClickListener(event -> openTicket());

        return ticketDiv;
    }

    private void openTicket() {
        UI.getCurrent().navigate(TicketListView.class);
    }
}
