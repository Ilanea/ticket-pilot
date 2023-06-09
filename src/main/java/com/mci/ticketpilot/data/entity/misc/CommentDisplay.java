package com.mci.ticketpilot.data.entity.misc;

import com.mci.ticketpilot.data.entity.Comment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.List;

public class CommentDisplay extends VerticalLayout {

    public CommentDisplay(List<Comment> comments) {
        if (comments != null) {
            for (Comment comment : comments) {
                TextArea label = new TextArea(comment.getAuthor() + "@" + comment.getTimestamp());
                label.setValue(comment.getComment());
                label.setWidthFull();
                label.setReadOnly(true);
                add(label);
            }
        }
    }

    public void setComments(List<Comment> comments) {
        removeAll();
        if (comments != null) {
            for (Comment comment : comments) {
                TextArea label = new TextArea(comment.getAuthor() + "@" + comment.getTimestamp());
                label.setValue(comment.getComment());
                label.setWidthFull();
                label.setReadOnly(true);
                add(label);
            }
        }
    }
}
