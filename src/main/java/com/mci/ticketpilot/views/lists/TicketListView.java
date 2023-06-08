package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.service.PilotService;
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
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.html.Anchor;

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
    private HorizontalLayout formAndCommentsLayout;
    private Div gridContainer;
    private Div formContainer;
    private Div commentsContainer;
    private Button createTicketButton;
    private Button backButton;
    private Button exportToExcelButton;


    public TicketListView(PilotService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureForm();
        configureGrid();

        add(getToolbar(), getGridContainer(), getFormAndCommentsLayout());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getFormAndCommentsLayout() {
        formAndCommentsLayout = new HorizontalLayout(getFormContainer(), getCommentsContainer());
        formAndCommentsLayout.setSizeFull();
        formAndCommentsLayout.setFlexGrow(1, formContainer);
        formAndCommentsLayout.setFlexGrow(1, commentsContainer);
        formAndCommentsLayout.setVisible(false);
        return formAndCommentsLayout;
    }

    private Div getGridContainer() {
        gridContainer = new Div(grid);
        gridContainer.addClassName("grid-container");
        gridContainer.setSizeFull();
        return gridContainer;
    }

    private void configureGrid() {
        grid.addClassNames("ticket-grid");
        grid.setSizeFull();
        grid.setColumns("ticketName", "ticketPriority", "ticketStatus", "project.projectName", "user");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editTicket(event.getValue(), false));
    }

    private Div getCommentsContainer() {
        commentsContainer = new Div();
        commentsContainer.addClassName("comments-container");
        commentsContainer.setVisible(false);
        commentsContainer.setSizeFull();
        return commentsContainer;
    }

    private Div getFormContainer() {
        formContainer = new Div(form);
        formContainer.addClassName("form-container");
        formContainer.setVisible(false);
        formContainer.setSizeFull();
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

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        this.createTicketButton = new Button("Create Ticket");
        createTicketButton.addClickListener(click -> createTicket());

        this.backButton = new Button("Back to List");
        backButton.addClickListener(click -> closeEditor());
        backButton.setVisible(false);

        // Button for Excel export
        this.exportToExcelButton = new Button("Export to Excel");
        StreamResource sr = new StreamResource("tickets.xlsx", () -> service.exportToExcel(filterText.getValue()));
        Anchor downloadLink = new Anchor(sr, "");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(exportToExcelButton);

        exportToExcelButton.addClickListener(event -> downloadLink.setHref(sr));

        var toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        toolbar.add(filterText, createTicketButton, backButton, downloadLink);

        return toolbar;
    }


    public void editTicket(Ticket ticket, boolean isNew) {
        if (ticket == null) {
            closeEditor();
        } else {
            form.setTicket(ticket);

            formAndCommentsLayout.setVisible(true);
            gridContainer.setVisible(false);
            formContainer.setVisible(true);
            createTicketButton.setVisible(false);
            backButton.setVisible(true);
            exportToExcelButton.setVisible(false);

            addClassName("editing");

            if(!isNew){
                commentsContainer.setVisible(true);

                TicketComment ticketComment = new TicketComment(service, ticket);
                commentsContainer.removeAll();
                commentsContainer.add(ticketComment);
            }
        }
    }

    private void closeEditor() {
        form.setTicket(null);

        formAndCommentsLayout.setVisible(false);
        gridContainer.setVisible(true);
        formContainer.setVisible(false);
        commentsContainer.setVisible(false);
        createTicketButton.setVisible(true);
        backButton.setVisible(false);
        exportToExcelButton.setVisible(true);

        removeClassName("editing");
    }

    private void createTicket() {
        grid.asSingleSelect().clear();

        // Visibilities
        //formAndCommentsLayout.setVisible(true);
        gridContainer.setVisible(false);
        formContainer.setVisible(true);
        commentsContainer.setVisible(false);
        createTicketButton.setVisible(false);
        backButton.setVisible(true);
        exportToExcelButton.setVisible(false);

        editTicket(new Ticket(), true);
    }

    private void updateList() {
        grid.setItems(service.findAllTickets(filterText.getValue()));
    }
}
