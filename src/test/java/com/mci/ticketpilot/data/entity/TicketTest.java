package com.mci.ticketpilot.data.entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketTest {

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Ticket ticket = new Ticket();

        // Set values using setters
        String ticketName = "Test Ticket";
        ticket.setTicketName(ticketName);

        String ticketDescription = "This is a test ticket.";
        ticket.setTicketDescription(ticketDescription);

        LocalDate ticketCreationDate = LocalDate.now();
        ticket.setTicketCreationDate(ticketCreationDate);

        LocalDate ticketLastUpdateDate = LocalDate.now();
        ticket.setTicketLastUpdateDate(ticketLastUpdateDate);

        TicketStatus ticketStatus = TicketStatus.IN_PROGRESS;
        ticket.setTicketStatus(ticketStatus);

        TicketPriority ticketPriority = TicketPriority.HIGH;
        ticket.setTicketPriority(ticketPriority);

        Project project = new Project();
        ticket.setProject(project);

        Users assignee = new Users();
        ticket.setAssignee(assignee);

        Users author = new Users();
        ticket.setAuthor(author);

        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        comments.add(comment1);
        comments.add(comment2);
        ticket.addComment(comment1);
        ticket.addComment(comment2);

        List<Document> documents = new ArrayList<>();
        Document document1 = new Document();
        Document document2 = new Document();
        documents.add(document1);
        documents.add(document2);
        ticket.getDocuments().addAll(documents);

        LocalDate dueDate = LocalDate.now().plusDays(7);
        ticket.setDueDate(dueDate);

        // Assert
        Assertions.assertEquals(ticketName, ticket.getTicketName());
        Assertions.assertEquals(ticketDescription, ticket.getTicketDescription());
        Assertions.assertEquals(ticketCreationDate, ticket.getTicketCreationDate());
        Assertions.assertEquals(ticketLastUpdateDate, ticket.getTicketLastUpdateDate());
        Assertions.assertEquals(ticketStatus, ticket.getTicketStatus());
        Assertions.assertEquals(ticketPriority, ticket.getTicketPriority());
        Assertions.assertEquals(project, ticket.getProject());
        Assertions.assertEquals(assignee, ticket.getAssignee());
        Assertions.assertEquals(author, ticket.getAuthor());
        Assertions.assertEquals(comments, ticket.getComments());
        Assertions.assertEquals(documents, ticket.getDocuments());
        Assertions.assertEquals(dueDate, ticket.getDueDate());
    }

    @Test
    public void testEmptyTicketName() {
        // Arrange
        Ticket ticket = new Ticket();

        // Act
        ticket.setTicketName("");

        // Assert
        Assertions.assertTrue(ticket.getTicketName().isEmpty());
    }
}
