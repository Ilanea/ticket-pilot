package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.*;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketForm extends FormLayout {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private PilotService service;
    private Project selectedProject;
    private Users selectedUser;
    TextField ticketName = new TextField("Title");
    ComboBox<TicketPriority> ticketPriority = new ComboBox<>("Priority");
    ComboBox<TicketStatus> ticketStatus = new ComboBox<>("Status");
    ComboBox<Project> linkedProject = new ComboBox<>("Project");
    ComboBox<Users> linkedUser = new ComboBox<>("User");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Ticket> binder = new BeanValidationBinder<>(Ticket.class);

    public TicketForm(List<Ticket> tickets, PilotService service) {
        this.service = service;

        // Bind Form Fields to Object
        addClassName("ticket-form");
        binder.forField(linkedProject).bind(Ticket::getProject, Ticket::setProject);
        binder.forField(linkedUser).bind(Ticket::getUser, Ticket::setUser);
        binder.bindInstanceFields(this);

        ticketPriority.setItems(TicketPriority.values());
        ticketStatus.setItems(TicketStatus.values());

        List<Project> projects = service.findAllProjects();
        List<Users> users = service.findAllUsers();

        linkedProject.setClearButtonVisible(true);
        linkedProject.setPrefixComponent(VaadinIcon.SEARCH.create());
        linkedUser.setClearButtonVisible(true);
        linkedUser.setPrefixComponent(VaadinIcon.SEARCH.create());

        linkedProject.setItems(projects);
        linkedUser.setItems(users);

        linkedProject.setItemLabelGenerator(Project::getProjectName);
        linkedUser.setItemLabelGenerator(user -> user.getFirstName() + " " + user.getLastName());

        linkedProject.addValueChangeListener(event -> {
            selectedProject = event.getValue();
        });

        linkedUser.addValueChangeListener(event -> {
            selectedUser = event.getValue();
        });

        add(ticketName, ticketPriority, ticketStatus, linkedProject, linkedUser, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new TicketForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new TicketForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            Ticket ticket = binder.getBean();
            // Saving a ticket without a project gets a NullPointerException
            // This hack is to circumvent this issue
            if (ticket.getProject() == null) {
                Notification.show("Please select a project", 2000, Notification.Position.MIDDLE);
            } else {
                fireEvent(new TicketForm.SaveEvent(this, ticket));
            }
        }
    }


    public void setTicket(Ticket ticket) {
        if(ticket != null){
            binder.setBean(ticket);
            logger.info("Set selected Ticket to: " + ticket);
        }
    }

    // Events
    public static abstract class TicketFormEvent extends ComponentEvent<TicketForm> {
        private Ticket ticket;

        protected TicketFormEvent(TicketForm source, Ticket ticket) {
            super(source, false);
            this.ticket = ticket;
        }

        public Ticket getTicket() {
            return ticket;
        }
    }

    public static class SaveEvent extends TicketForm.TicketFormEvent {
        SaveEvent(TicketForm source, Ticket ticket) {
            super(source, ticket);
        }
    }

    public static class DeleteEvent extends TicketForm.TicketFormEvent {
        DeleteEvent(TicketForm source, Ticket ticket) {
            super(source, ticket);
        }

    }

    public static class CloseEvent extends TicketForm.TicketFormEvent {
        CloseEvent(TicketForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<TicketForm.DeleteEvent> listener) {
        return addListener(TicketForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<TicketForm.SaveEvent> listener) {
        return addListener(TicketForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<TicketForm.CloseEvent> listener) {
        return addListener(TicketForm.CloseEvent.class, listener);
    }
}
