package com.mci.ticketpilot.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Users extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "integer default 0")
    private UserStatus userStatus;


    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "integer default 1")
    private UserRole userRole;


    @Email
    @NotEmpty
    private String email = "";

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

}
