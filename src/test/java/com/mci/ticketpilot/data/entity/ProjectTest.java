package com.mci.ticketpilot.data.entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ProjectTest {

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Project project = new Project();

        // Set values using setters
        String projectName = "Test Project";
        project.setProjectName(projectName);

        String projectDescription = "This is a test project.";
        project.setProjectDescription(projectDescription);

        LocalDate projectStartDate = LocalDate.now();
        project.setProjectStartDate(projectStartDate);

        LocalDate projectEndDate = LocalDate.now().plusDays(30);
        project.setProjectEndDate(projectEndDate);

        Users projectManager = new Users();
        project.setManager(projectManager);

        Set<Ticket> projectTickets = new HashSet<>();
        project.setTickets(projectTickets);

        // Assert
        Assertions.assertEquals(projectName, project.getProjectName());
        Assertions.assertEquals(projectDescription, project.getProjectDescription());
        Assertions.assertEquals(projectStartDate, project.getProjectStartDate());
        Assertions.assertEquals(projectEndDate, project.getProjectEndDate());
        Assertions.assertEquals(projectManager, project.getManager());
        Assertions.assertEquals(projectTickets, project.getTickets());
    }

    @Test
    public void testEmptyProjectName() {
        // Arrange
        Project project = new Project();

        // Act
        project.setProjectName("");

        // Assert
        Assertions.assertTrue(project.getProjectName().isEmpty());
    }
}



