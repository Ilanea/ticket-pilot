package com.mci.ticketpilot.data.entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class UsersTest {

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Users user = new Users();
        String firstName = "John";
        String lastName = "Doe";
        String email = "johndoe@example.com";
        String password = "password";
        UserStatus userStatus = UserStatus.ACTIVE;
        UserRole userRole = UserRole.USER;
        Set<Project> projects = new HashSet<>();
        Set<Ticket> tickets = new HashSet<>();

        // Act
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserStatus(userStatus);
        user.setUserRole(userRole);
        user.setProject(projects);
        user.setUserTickets(tickets);

        // Assert
        Assertions.assertEquals(firstName, user.getFirstName());
        Assertions.assertEquals(lastName, user.getLastName());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(password, user.getPassword());
        Assertions.assertEquals(userStatus, user.getUserStatus());
        Assertions.assertEquals(userRole, user.getUserRole());
        Assertions.assertEquals(projects, user.getProject());
        Assertions.assertEquals(tickets, user.getUserTickets());
    }

    @Test
    public void testToString() {
        // Arrange
        Users user = new Users();
        String firstName = "John";
        String lastName = "Doe";
        user.setFirstName(firstName);
        user.setLastName(lastName);

        // Act
        String fullName = user.toString();

        // Assert
        Assertions.assertEquals(firstName + " " + lastName, fullName);
    }
}
