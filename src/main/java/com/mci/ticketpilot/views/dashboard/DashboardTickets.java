package com.mci.ticketpilot.views.dashboard;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.TicketPriority;
import com.mci.ticketpilot.data.entity.TicketStatus;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.views.lists.TicketComment;
import com.mci.ticketpilot.views.lists.TicketDocument;
import com.mci.ticketpilot.views.lists.TicketForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
@PermitAll
public class DashboardTickets extends VerticalLayout {
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
    private Button backButton;
    MultiSelectComboBox<TicketPriority> ticketPriority = new MultiSelectComboBox<>();
    MultiSelectComboBox<TicketStatus> ticketStatus = new MultiSelectComboBox<>();


    public DashboardTickets(PilotService service) {
        this.service = service;
        addClassName("dashboard-tickets-view");
        setSizeFull();
        configureGrid();
        configureForm();

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
        gridContainer.addClassName("dashboard-tickets-grid-container");
        gridContainer.setSizeFull();
        return gridContainer;
    }

    private void configureGrid() {
        grid.setColumns("ticketName", "ticketPriority", "ticketStatus", "project.projectName", "ticketCreationDate");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editTicket(event.getValue(), false));
    }

    private Div getCommentsContainer() {
        commentsContainer = new Div();
        commentsContainer.addClassName("dashboard-tickets-comments-container");
        commentsContainer.setVisible(false);
        commentsContainer.setSizeFull();
        return commentsContainer;
    }

    private Div getFormContainer() {
        formContainer = new Div(form);
        formContainer.addClassName("dashboard-tickets-form-container");
        formContainer.setVisible(false);
        formContainer.setSizeFull();
        return formContainer;
    }

    private Div getDocumentContainer() {
        documentsContainer = new Div();
        documentsContainer.addClassName("dashboard-tickets-document-container");
        documentsContainer.setVisible(false);
        documentsContainer.setSizeFull();
        return documentsContainer;
    }

    private void configureForm() {
        form = new TicketForm(service.findAllTicketsForUser(), service);
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
        Notification notification = new Notification("Ticket saved", 3000);
        notification.setPosition(Notification.Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
    }

    private void deleteTicket(TicketForm.DeleteEvent event) {
        service.deleteTicket(event.getTicket());
        updateList();
        closeEditor();
        // Benachrichtigung erstellen und anzeigen
        Notification notification = new Notification("Ticket deleted", 3000);
        notification.setPosition(Notification.Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        ticketPriority.setItems(TicketPriority.values());
        ticketPriority.select(TicketPriority.values());
        ticketPriority.addValueChangeListener(e -> updateList());

        ticketStatus.setItems(TicketStatus.values());
        ticketStatus.select(TicketStatus.values());
        ticketStatus.addValueChangeListener(e -> updateList());

        this.backButton = new Button("Back to List");
        backButton.addClickListener(click -> closeEditor());
        backButton.setVisible(false);

        var toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        toolbar.add(filterText, ticketPriority, ticketStatus, backButton);

        return toolbar;
    }


    public void editTicket(Ticket ticket, boolean isNew) {
        if (ticket == null) {
            closeEditor();
        } else {
            addClassName("editing");
            form.setTicket(ticket);

            filterText.setVisible(false);
            ticketPriority.setVisible(false);
            ticketStatus.setVisible(false);
            formAndDocumentsLayout.setVisible(true);
            contentLayout.setVisible(true);
            gridContainer.setVisible(false);
            formContainer.setVisible(true);
            backButton.setVisible(true);


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

    private void closeEditor() {
        form.setTicket(null);

        filterText.setVisible(true);
        ticketPriority.setVisible(true);
        ticketStatus.setVisible(true);
        formAndDocumentsLayout.setVisible(false);
        contentLayout.setVisible(false);
        commentLayout.setVisible(false);
        gridContainer.setVisible(true);
        formContainer.setVisible(false);
        commentsContainer.setVisible(false);
        documentsContainer.setVisible(false);
        backButton.setVisible(false);

        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllTicketsForUser(filterText.getValue(), ticketStatus.getValue(), ticketPriority.getValue()));
    }
}
