package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
    private VerticalLayout formAndDocumentsLayout;
    private HorizontalLayout commentLayout;
    private HorizontalLayout contentLayout;
    private Div gridContainer;
    private Div formContainer;
    private Div commentsContainer;
    private Div documentsContainer;
    private Button createTicketButton;
    private Button backButton;
    private Button exportToExcelButton;


    public TicketListView(PilotService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureForm();
        configureGrid();

        contentLayout = new HorizontalLayout(getFormAndDocumentsLayout(), getCommentsLayout());
        contentLayout.setSizeFull();
        contentLayout.setVisible(false);

        add(getToolbar(), getGridContainer(), contentLayout);
        updateList();
        closeEditor();
    }

    private VerticalLayout getFormAndDocumentsLayout() {
        formAndDocumentsLayout = new VerticalLayout(getFormContainer(), getDocumentContainer());
        formAndDocumentsLayout.setSizeFull();
        formAndDocumentsLayout.setFlexGrow(1, formContainer);
        formAndDocumentsLayout.setFlexGrow(1, documentsContainer);
        formAndDocumentsLayout.setVisible(false);
        return formAndDocumentsLayout;
    }

    private HorizontalLayout getCommentsLayout() {
        commentLayout = new HorizontalLayout(getCommentsContainer());
        commentLayout.setSizeFull();
        commentLayout.setFlexGrow(1, commentsContainer);
        setHorizontalComponentAlignment(Alignment.CENTER, commentsContainer);
        commentLayout.setVisible(false);
        return commentLayout;
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
        grid.setColumns("ticketName", "ticketPriority", "ticketStatus", "project.projectName", "assignee", "ticketCreationDate");
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

    private Div getDocumentContainer() {
        documentsContainer = new Div();
        documentsContainer.addClassName("document-container");
        documentsContainer.setVisible(false);
        documentsContainer.setSizeFull();
        return documentsContainer;
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
        // Benachrichtigung erstellen und anzeigen
        Notification notification = new Notification("Ticket wurde erstellt!", 3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
    }

    private void deleteTicket(TicketForm.DeleteEvent event) {
        service.deleteTicket(event.getTicket());
        updateList();
        closeEditor();
        // Benachrichtigung erstellen und anzeigen
        Notification notification = new Notification("Ticket wurde gelöscht!", 3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
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

            formAndDocumentsLayout.setVisible(true);
            contentLayout.setVisible(true);
            gridContainer.setVisible(false);
            formContainer.setVisible(true);
            createTicketButton.setVisible(false);
            backButton.setVisible(true);
            exportToExcelButton.setVisible(false);

            addClassName("editing");

            if(!isNew){
                commentLayout.setVisible(true);


                commentsContainer.setVisible(true);
                TicketComment ticketComment = new TicketComment(service, ticket);
                commentsContainer.removeAll();
                commentsContainer.add(ticketComment);

                documentsContainer.setVisible(true);
                TicketDocument ticketDocument = new TicketDocument(service, ticket);
                documentsContainer.removeAll();
                documentsContainer.add(ticketDocument);
            }
        }
    }

    private void closeEditor() {
        form.setTicket(null);

        formAndDocumentsLayout.setVisible(false);
        contentLayout.setVisible(false);
        commentLayout.setVisible(false);
        gridContainer.setVisible(true);
        formContainer.setVisible(false);
        commentsContainer.setVisible(false);
        documentsContainer.setVisible(false);
        createTicketButton.setVisible(true);
        backButton.setVisible(false);
        exportToExcelButton.setVisible(true);

        removeClassName("editing");
    }

    private void createTicket() {
        grid.asSingleSelect().clear();

        // Visibilities
        gridContainer.setVisible(false);
        formContainer.setVisible(true);
        commentsContainer.setVisible(false);
        documentsContainer.setVisible(false);
        createTicketButton.setVisible(false);
        backButton.setVisible(true);
        exportToExcelButton.setVisible(false);

        editTicket(new Ticket(), true);
    }

    private void updateList() {
        grid.setItems(service.findAllTickets(filterText.getValue()));
    }
}
