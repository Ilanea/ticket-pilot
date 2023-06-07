package com.mci.ticketpilot.data.entity.misc;

import com.mci.ticketpilot.data.entity.Comment;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class CommentDisplay extends VerticalLayout {

    public CommentDisplay(List<Comment> comments) {
        if (comments != null) {
            for (Comment comment : comments) {
                Label label = new Label(comment.getAuthor() + ": " + comment.getComment());
                add(label);
            }
        }
    }

    public void setComments(List<Comment> comments) {
        removeAll();
        if (comments != null) {
            for (Comment comment : comments) {
                Label label = new Label(comment.getAuthor() + ": " + comment.getComment());
                add(label);
            }
        }
    }
}
