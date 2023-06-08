package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Comment;
import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.misc.CommentDisplay;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketComment extends VerticalLayout {

    private PilotService service;
    private Ticket currentTicket;
    private TextField newCommentField = new TextField();
    private CommentDisplay commentDisplay;
    private Div commentDisplayContainer = new Div();
    Button postCommentButton = new Button("Post Comment");


    public TicketComment(PilotService service,Ticket ticket) {
        this.service = service;
        this.currentTicket = ticket;

        if(commentDisplay == null){
            commentDisplay = new CommentDisplay(currentTicket.getComments());
            commentDisplayContainer.setWidthFull();
            commentDisplayContainer.getStyle().set("margin-top", "10px");
            commentDisplayContainer.getStyle().set("overflow-y", "auto");
            commentDisplayContainer.getStyle().set("height", "400px");
            commentDisplayContainer.add(commentDisplay);
            add(commentDisplayContainer);
        } else {
            commentDisplay.setComments(currentTicket.getComments());
        }

        postCommentButton.addClickListener(e -> {
            if (currentTicket != null) {
                if(!newCommentField.getValue().isEmpty()){
                    addComment(newCommentField.getValue(), currentTicket);
                }
            }
        });

        setAlignItems(FlexComponent.Alignment.CENTER);

        newCommentField.setLabel("New Comment");
        newCommentField.setPlaceholder("Enter your comment here");
        newCommentField.getStyle().set("width", "100%");

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

            TextArea label = new TextArea(newComment.getAuthor() + "@" + newComment.getTimestamp());
            label.setValue(newComment.getComment());
            label.setWidthFull();
            label.setEnabled(false);

            commentDisplay.add(label);
            newCommentField.clear();
        }
    }
}
