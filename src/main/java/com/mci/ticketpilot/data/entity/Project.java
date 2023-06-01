package com.mci.ticketpilot.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.Set;

@Entity
public class Project extends AbstractEntity {

    @NotEmpty
    private String projectName;

    // Each Project can have one Manager
    @ManyToOne(fetch = FetchType.EAGER)
    private Users projectManager;

    // Each Project can have many Tickets
    @OneToMany(mappedBy = "ticketProject", fetch = FetchType.EAGER)
    private Set<Ticket> projectTickets;
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String title) {
        this.projectName = title;
    }

    public Set<Ticket> getTickets() {
        return projectTickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.projectTickets = tickets;
    }

    public Users getManager() {
        return projectManager;
    }

    public void setManager(Users manager) {
        this.projectManager = manager;
    }
}
