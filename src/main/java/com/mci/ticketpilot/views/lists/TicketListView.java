package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;



@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "tickets", layout = MainLayout.class)
@PageTitle("Tickets | Ticket Pilot")
public class TicketListView extends VerticalLayout {
    private Grid<Ticket> grid = new Grid<>(Ticket.class);
    private TextField filterText = new TextField();
    private TicketForm form;
    private PilotService service;
    private Div gridContainer;
    private Div formContainer;


    public TicketListView(PilotService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getGridContainer(), getFormContainer());
        updateList();
        closeEditor();
    }

    private Div getGridContainer() {
        gridContainer = new Div(grid);
        gridContainer.addClassName("grid-container");
        gridContainer.setSizeFull();
        return gridContainer;
    }

    private Div getFormContainer() {
        formContainer = new Div(form);
        formContainer.addClassName("form-container");
        formContainer.setVisible(false);
        return formContainer;
    }

    private void configureForm() {
        form = new TicketForm(service.findAllTickets(), service);
        form.setSizeFull();
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

        grid.asSingleSelect().addValueChangeListener(event -> editTicket(event.getValue()));
    }
    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button createTicketButton = new Button("Create Ticket");
        createTicketButton.addClickListener(click -> createTicket());

        // Button for Excel export
        Button exportToExcelButton = new Button("Export to Excel");
        StreamResource sr = new StreamResource("tickets.xlsx", () -> service.exportToExcel(filterText.getValue()));
        Anchor downloadLink = new Anchor(sr, "");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(exportToExcelButton);

        exportToExcelButton.addClickListener(event -> downloadLink.setHref(sr));

        var toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        toolbar.add(filterText, createTicketButton, downloadLink); // Add Excel button to the toolbar

        return toolbar;
    }


    public void editTicket(Ticket ticket) {
        if (ticket == null) {
            closeEditor();
        } else {
            form.setTicket(ticket);
            gridContainer.setVisible(false);
            formContainer.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setTicket(null);
        gridContainer.setVisible(true);
        formContainer.setVisible(false);
        form.setTicket(null);
        removeClassName("editing");
    }

    private void createTicket() {
        grid.asSingleSelect().clear();
        gridContainer.setVisible(false);
        formContainer.setVisible(true);
        editTicket(new Ticket());
    }

    private void updateList() {
        grid.setItems(service.findAllTickets(filterText.getValue()));
    }
}
