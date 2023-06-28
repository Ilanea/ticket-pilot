package com.mci.ticketpilot.data.service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.entity.Ticket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
            document.add(new Paragraph(project.getProjectName()));
        }

        document.add(new Paragraph("Tickets:"));
        for (Ticket ticket : userTickets) {
            document.add(new Paragraph(ticket.getTicketName()));
        }

        document.close();

        return baos.toByteArray();
    }
}
