package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.*;
import com.mci.ticketpilot.data.entity.misc.CommentDisplay;
import com.mci.ticketpilot.data.service.ApplicationContextProvider;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.data.service.SendMailService;
import com.mci.ticketpilot.security.SecurityService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import java.time.LocalDateTime;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.flow.component.html.Label;import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;




public class TicketForm extends FormLayout {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private PilotService service;
    private Project selectedProject;
    private TextField comment = new TextField("Comment");
    private Users selectedUser;
    private Ticket currentTicket;
    private TextField newCommentField = new TextField();
    private CommentDisplay commentDisplay;

    TextField ticketName = new TextField("Title");
    ComboBox<TicketPriority> ticketPriority = new ComboBox<>("Priority");
    ComboBox<TicketStatus> ticketStatus = new ComboBox<>("Status");
    ComboBox<Project> linkedProject = new ComboBox<>("Project");
    ComboBox<Users> linkedUser = new ComboBox<>("User");
    TextArea ticketDescription = new TextArea("Description");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Button postCommentButton = new Button("Post Comment");

    Binder<Ticket> binder = new BeanValidationBinder<>(Ticket.class);


    public TicketForm(List<Ticket> tickets, PilotService service) {
        this.service = service;

        // Bind Form Fields to Object
        addClassName("ticket-form");
        binder.forField(linkedProject).bind(Ticket::getProject, Ticket::setProject);
        binder.forField(linkedUser).bind(Ticket::getUser, Ticket::setUser);
        binder.bindInstanceFields(this);

        // Priority
        ticketPriority.setItems(TicketPriority.values());
        ticketStatus.setItems(TicketStatus.values());

        // Linkedproject & Linkeduser
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

        // Ticket description
        ticketDescription.setMaxLength(300);
        ticketDescription.setValueChangeMode(ValueChangeMode.EAGER);
        ticketDescription.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + 300);
        });

        add(ticketName, ticketDescription, ticketStatus, linkedProject, linkedUser, createButtonsLayout(),
                newCommentField, postCommentButton);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        postCommentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> {
            try {
                validateAndSave();
            } catch (EmailException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        delete.addClickListener(event -> fireEvent(new TicketForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new TicketForm.CloseEvent(this)));

        postCommentButton.addClickListener(e -> {
            if (currentTicket != null) {
                addComment(newCommentField.getValue(), currentTicket);
            }
        });

        postCommentButton.addClickListener(event-> {
            if (currentTicket != null) {
                fireEvent(new TicketForm.CommentEvent(this, binder.getBean()));
            }
        });


        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() throws EmailException, IOException {
        if (binder.isValid()) {
            Ticket ticket = binder.getBean();
            // Saving a ticket without a project gets a NullPointerException
            // This hack is to circumvent this issue
            if (ticket.getProject() == null) {
                Notification.show("Please select a project", 2000, Notification.Position.MIDDLE);
            } else {
                // If the ticket is new, set the current user as the creator
                SendMailService sendMail = ApplicationContextProvider.getApplicationContext().getBean(SendMailService.class);
                try {
                    sendMail.send(ticket.getUser().getEmail(), ticket.getUser().getFirstName(), ticket.getUser().getLastName(), ticket.getTicketName(), ticket.getTicketDescription());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fireEvent(new TicketForm.SaveEvent(this, ticket));
            }
        }
    }

    public void setTicket(Ticket ticket) {
        if(ticket != null){
            this.currentTicket = ticket;  // Add this line
            binder.setBean(ticket);

            // Assignee can only be changed by Admins, Managers or the current assignee
            if (SecurityUtils.userHasAdminRole() || SecurityUtils.userHasManagerRole() || service.isCurrentUserAssignee(ticket)) {
                linkedUser.setReadOnly(false);
                if(commentDisplay == null){
                    commentDisplay = new CommentDisplay(ticket.getComments());
                    add(commentDisplay);
                }
            } else {
                linkedUser.setReadOnly(true);
            }
            logger.info("Set selected Ticket to: " + ticket);
        }
    }

    private void addComment(String commentText, Ticket ticket) {
        if (ticket != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            String formattedTime = LocalDateTime.now().format(formatter);

            Comment newComment = new Comment(commentText, SecurityUtils.getLoggedInUser(), formattedTime);// Set the document data

            newComment.setTicket(ticket);  // assuming Comment class has setTicket method
            ticket.getComments().add(newComment);
            service.saveTicket(ticket);
            commentDisplay.add(new Label(newComment.getAuthor() + " " + newComment.getTimestamp() + ": " + newComment.getComment()));
            newCommentField.clear();
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

    public static class CommentEvent extends TicketForm.TicketFormEvent {
        CommentEvent(TicketForm source, Ticket ticket) {
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

    public Registration addCommentListener(ComponentEventListener<TicketForm.CommentEvent> listener) {
        return addListener(TicketForm.CommentEvent.class, listener);
}}
