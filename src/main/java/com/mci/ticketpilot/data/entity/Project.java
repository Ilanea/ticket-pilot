package com.mci.ticketpilot.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project extends AbstractEntity {

    @NotEmpty
    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users manager;
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String title) {
        this.projectName = title;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Users getManager() {
        return manager;
    }

    public void setManager(Users manager) {
        this.manager = manager;
    }
}
