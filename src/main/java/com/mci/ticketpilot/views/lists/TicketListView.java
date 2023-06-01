package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "tickets", layout = MainLayout.class)
@PageTitle("Tickets | Ticket Pilot")
public class TicketListView extends VerticalLayout {
    Grid<Ticket> grid = new Grid<>(Ticket.class);
    TextField filterText = new TextField();
    TicketForm form;
    PilotService service;


    public TicketListView(PilotService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new TicketForm(service.findAllTickets(), service);
        form.setWidth("25em");
        form.addSaveListener(this::saveTicket);
        form.addDeleteListener(this::deleteTicket);
        form.addCloseListener(e -> closeEditor());

    }

    private void saveTicket(TicketForm.SaveEvent event) {
        service.saveTicket(event.getTicket());
        updateList();
        closeEditor();
    }

    private void deleteTicket(TicketForm.DeleteEvent event) {
        service.deleteTicket(event.getTicket());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("ticket-grid");
        grid.setSizeFull();
        grid.setColumns("ticketName", "ticketPriority", "ticketStatus", "project.projectName", "user");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editTicket(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button createTicketButton = new Button("Create Ticket");
        createTicketButton.addClickListener(click -> createTicket());

        var toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        toolbar.add(filterText, createTicketButton);

        return toolbar;
    }

    public void editTicket(Ticket ticket) {
        if (ticket == null) {
            closeEditor();
        } else {
            form.setTicket(ticket);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setTicket(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void createTicket() {
        grid.asSingleSelect().clear();
        editTicket(new Ticket());
    }

    private void updateList() {
        grid.setItems(service.findAllTickets(filterText.getValue()));
    }
}
