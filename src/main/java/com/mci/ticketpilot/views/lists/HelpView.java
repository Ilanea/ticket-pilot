package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.service.ApplicationContextProvider;
import com.mci.ticketpilot.data.service.SendMailService;
import com.mci.ticketpilot.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "help", layout = MainLayout.class)
@PageTitle("Help | Ticket Pilot")
public class HelpView extends VerticalLayout {

    private final SendMailService sendMailService;



    public HelpView(@Autowired SendMailService sendMailService) {

        this.sendMailService = sendMailService;
        H1 header = new H1("Help");
        Paragraph intro = new Paragraph("Welcome to the Ticket Pilot Help Center. Here, you will find tips and guides on how to use the Ticket Pilot platform.");

        H2 ticketSection = new H2("Managing Tickets");
        Paragraph ticketText = new Paragraph("You can create, update, and delete tickets. To view details of a ticket, click on the ticket name. To edit a ticket, click the 'Edit' button.");

        H2 projectSection = new H2("Managing Projects");
        Paragraph projectText = new Paragraph("Projects can be created and managed by administrators and managers. Click on a project name to view its details.");

        H2 userSection = new H2("Managing Users");
        Paragraph userText = new Paragraph("User management is restricted to administrators and managers. To create a user, click on the 'Add User' button.");

        H2 searchSection = new H2("Searching and Filtering");
        Paragraph searchText = new Paragraph("You can search for specific tickets or projects by typing in the search bar at the top of the screen. You can also filter tickets by status, priority, and assignment.");

        H2 notificationSection = new H2("Notifications");
        Paragraph notificationText = new Paragraph("You will receive notifications for ticket updates and assignments. You can view and manage your notifications by clicking the 'Bell' icon.");

        H2 faqSection = new H2("Frequently Asked Questions");
        H2 question1 = new H2("Q: How do I reset my password?");
        Paragraph answer1 = new Paragraph("A: Click on your profile icon at the top right of the screen, then select 'Change Password'. You will need to know your current password to set a new one.");
        H2 question2 = new H2("Q: How do I delete a ticket?");
        Paragraph answer2 = new Paragraph("A: Click on the ticket you want to delete, then click the 'Delete' button. Note that this action is irreversible.");

        add(header, intro, ticketSection, ticketText, projectSection, projectText, userSection, userText, searchSection, searchText, notificationSection, notificationText, faqSection, question1, answer1, question2, answer2);

        H2 feedbackSection = new H2("Submit Feedback");
        Paragraph feedbackText = new Paragraph("We value your feedback. Please provide your comments and suggestions below.");

// Create a text area for feedback submission
        TextArea feedbackArea = new TextArea();
        feedbackArea.setPlaceholder("Write your feedback here...");
        feedbackArea.setWidthFull();

// Create a button to submit feedback
        Button submitButton = new Button("Submit", event -> {
            String feedback = feedbackArea.getValue();
            if (!feedback.trim().isEmpty()) {
                SendMailService sendMail = ApplicationContextProvider.getApplicationContext().getBean(SendMailService.class);

                //sendMail.send(ticket.getUser().getEmail(), ticket.getUser().getFirstName(), ticket.getUser().getLastName(), ticket.getTicketName(), ticket.getTicketDescription());
                Notification.show("Your feedback has been submitted. Thank you!");
                feedbackArea.clear(); // clear the text area
            } else {
                Notification.show("Feedback area cannot be empty.");
            }
        });
// Add feedback section components to the view
        add(feedbackSection, feedbackText, feedbackArea, submitButton);

    }
}
