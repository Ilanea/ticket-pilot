package com.mci.ticketpilot.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Ticket extends AbstractEntity {

    @NotEmpty
    private String ticketName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'OPEN'")
    private TicketStatus ticketStatus;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'DEFAULT'")
    private TicketPriority ticketPriority;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
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
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

