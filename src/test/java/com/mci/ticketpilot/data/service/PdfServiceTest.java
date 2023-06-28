package com.mci.ticketpilot.data.service;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.Document;
import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.entity.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PdfServiceTest {

    @Test
    public void createPdf_ReturnsNonEmptyByteArray() throws IOException {
        // Mock userProjects
        List<Project> userProjects = new ArrayList<>();
        userProjects.add(new Project("Project 1"));
        userProjects.add(new Project("Project 2"));

        // Mock userTickets
        List<Ticket> userTickets = new ArrayList<>();
        userTickets.add(new Ticket("Ticket 1"));
        userTickets.add(new Ticket("Ticket 2"));

        PdfService pdfService = new PdfService();
        byte[] pdfBytes = pdfService.createPdf(userProjects, userTickets);

        Assertions.assertNotNull(pdfBytes);
        Assertions.assertTrue(pdfBytes.length > 0);

        // Validate PDF content (optional)
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(Arrays.toString(pdfBytes)));
        Document document = new Document(pdfDocument);
        String pdfContent = document.toString();

        // Assert that the PDF content contains the expected project and ticket names
        Assertions.assertTrue(pdfContent.contains("Projects:"));
        Assertions.assertTrue(pdfContent.contains("Project 1"));
        Assertions.assertTrue(pdfContent.contains("Project 2"));
        Assertions.assertTrue(pdfContent.contains("Tickets:"));
        Assertions.assertTrue(pdfContent.contains("Ticket 1"));
        Assertions.assertTrue(pdfContent.contains("Ticket 2"));

        document.close();
    }
}

