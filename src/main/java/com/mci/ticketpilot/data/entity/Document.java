package com.mci.ticketpilot.data.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

@Entity
public class Document extends AbstractEntity{

    private String fileName;

    private String fileType;

    private LocalDate documentCreationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users author;

    @Column(columnDefinition = "BYTEA")
    private byte[] documentData;

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id", nullable = false)
    private Ticket ticket;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDate getDocumentCreationDate() {
        return documentCreationDate;
    }

    public void setDocumentCreationDate(LocalDate documentCreationDate) {
        this.documentCreationDate = documentCreationDate;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setFileTypeFromFileName() {
        String extension = String.valueOf(extractFileExtension(fileName));
        this.fileType = extension;
    }

    private Optional<String> extractFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return Optional.of(fileName.substring(dotIndex + 1));
        }
        return Optional.empty();
    }

}
