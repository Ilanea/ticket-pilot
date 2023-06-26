package com.mci.ticketpilot.data.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.entity.Ticket;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfService {
    public byte[] createPdf(List<Project> userProjects, List<Ticket> userTickets) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Stats"));

        document.add(new Paragraph("Projects:"));
        for (Project project : userProjects) {
            String projectName = project.getProjectName();
            if (projectName != null && !projectName.isEmpty()) {
                document.add(new Paragraph(projectName));
            }
        }

        document.add(new Paragraph("Tickets:"));
        for (Ticket ticket : userTickets) {
            String ticketName = ticket.getTicketName();
            if (ticketName != null && !ticketName.isEmpty()) {
                document.add(new Paragraph(ticketName));
            }
        }

        document.close();

        return baos.toByteArray();
    }
}