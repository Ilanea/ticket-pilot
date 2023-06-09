package com.mci.ticketpilot.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Entity
public class Users extends AbstractEntity {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, columnDefinition = "integer default 0")
    private UserStatus userStatus = UserStatus.ACTIVE;


    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, columnDefinition = "integer default 2")
    private UserRole userRole = UserRole.USER;


    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    // Each User can have many Projects
    @OneToMany(mappedBy = "projectManager", fetch = FetchType.EAGER)
    private Set<Project> userProjects;

    // Each User can have many Tickets
    @OneToMany(mappedBy = "assignee", fetch = FetchType.EAGER)
    private Set<Ticket> userTickets;

    // Getter and Setter methods
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Project> getProject() {
        return userProjects;
    }

    public void setProject(Set<Project> project) {
        this.userProjects = project;
    }

    public Set<Ticket> getUserTickets() {
        return userTickets;
    }

    public void setUserTickets(Set<Ticket> userTickets) {
        this.userTickets = userTickets;
    }
}
