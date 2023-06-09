package com.mci.ticketpilot.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;

@Entity
public class Comment extends AbstractEntity{

    @Size(max = 300)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users author;

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id", nullable = false)
    private Ticket ticket;

    private String timestamp;

    private byte[] documentData;

    // Constructor
    public Comment(String comment, Users author, String timestamp) {
        this.comment = comment;
        this.author = author;
        this.timestamp = timestamp;
    }

    public Comment() {

    }

    // Getter and Setter methods
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Getter and Setter methods for document data
    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }
}
