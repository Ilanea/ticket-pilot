package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Comment;
import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.misc.CommentDisplay;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketComment extends VerticalLayout {

    private PilotService service;
    private Ticket currentTicket;
    private TextField newCommentField = new TextField();
    private CommentDisplay commentDisplay;
    Button postCommentButton = new Button("Post Comment");


    public TicketComment(PilotService service,Ticket ticket) {
        this.service = service;
        this.currentTicket = ticket;

        if(currentTicket != null) {
            if(commentDisplay == null){
                commentDisplay = new CommentDisplay(currentTicket.getComments());
                add(commentDisplay);
            } else {
                commentDisplay.setComments(currentTicket.getComments());
            }
        }

        postCommentButton.addClickListener(e -> {
            if (currentTicket != null) {
                addComment(newCommentField.getValue(), currentTicket);
            }
        });

        newCommentField.setLabel("New Comment");
        newCommentField.setPlaceholder("Enter your comment here");

        add(newCommentField, postCommentButton);
    }



    private void addComment(String commentText, Ticket ticket) {
        if (ticket != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            String formattedTime = LocalDateTime.now().format(formatter);

            Comment newComment = new Comment(commentText, SecurityUtils.getLoggedInUser(), formattedTime);

            newComment.setTicket(ticket);
            ticket.getComments().add(newComment);

            service.saveTicket(ticket);

            commentDisplay.add(new Label(newComment.getAuthor() + "@" + newComment.getTimestamp() + ": " + newComment.getComment()));
            newCommentField.clear();
        }
    }
}
