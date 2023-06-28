package com.mci.ticketpilot.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket extends AbstractEntity {

    @NotEmpty
    private String ticketName;

    @Size(max = 300)
    private String ticketDescription;

    @Column(columnDefinition = "DATE")
    private LocalDate ticketCreationDate;

    @Column(columnDefinition = "DATE")
    private LocalDate ticketLastUpdateDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'OPEN'")
    private TicketStatus ticketStatus = TicketStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'DEFAULT'")
    private TicketPriority ticketPriority = TicketPriority.DEFAULT;

    // Each Ticket can be assigned to one Project
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="project_id", referencedColumnName = "id")
    private Project ticketProject;

    // Each Ticket can be assigned to one User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private Users assignee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private Users author;


    // One ticket can have multiple comments
    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Document> documents = new ArrayList<>();

    private LocalDate dueDate;


    // Getter and Setter methods
    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public TicketPriority getTicketPriority() {
        return ticketPriority;
    }

    public void setTicketPriority(TicketPriority ticketPriority) {
        this.ticketPriority = ticketPriority;
    }


    public Project getProject() {
        return ticketProject;
    }

    public void setProject(Project project) {
        this.ticketProject = project;
    }

    public Users getAssignee() {
        return assignee;
    }

    public void setAssignee(Users user) {
        this.assignee = user;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }


    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public LocalDate getTicketCreationDate() {
        // or any other default value
        return ticketCreationDate;
    }

    public void setTicketCreationDate(LocalDate ticketCreationDate) {
        this.ticketCreationDate = ticketCreationDate;
    }

    public LocalDate getTicketLastUpdateDate() {
        return ticketLastUpdateDate;
    }

    public void setTicketLastUpdateDate(LocalDate ticketLastUpdateDate) {
        this.ticketLastUpdateDate = ticketLastUpdateDate;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Document document) {
        this.documents.add(document);
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}

