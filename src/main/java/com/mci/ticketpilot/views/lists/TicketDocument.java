package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Document;
import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.service.PilotService;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class TicketDocument extends HorizontalLayout {
    private PilotService service;
    private Ticket currentTicket;
    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    private Div uploadedFilesDiv = new Div();

    public TicketDocument(PilotService service, Ticket ticket) {
        this.service = service;
        this.currentTicket = ticket;

        int maxFileSizeInBytes = 10 * 1024 * 1024;
        upload.setMaxFileSize(maxFileSizeInBytes);

        upload.addSucceededListener(event -> {
            InputStream inputStream = buffer.getInputStream();

            service.handleUploadedFile(inputStream, event.getFileName(), currentTicket);

            Notification.show("Upload succeeded!", 2000, Notification.Position.MIDDLE);

            refreshUploadedFiles();
        });

        upload.addFailedListener(event -> {
            Notification.show("Upload failed!", 2000, Notification.Position.MIDDLE);
        });

        refreshUploadedFiles();

        add(upload, uploadedFilesDiv);
    }


    private void refreshUploadedFiles() {
        List<Document> uploadedFiles = service.getUploadedFilesForTicket(currentTicket);

        uploadedFilesDiv.removeAll();
        for (Document file : uploadedFiles) {
            Anchor downloadLink = new Anchor(getDownloadLink(file), file.getFileName());
            downloadLink.setTarget("_blank");

            Div fileInfoDiv = new Div(downloadLink);
            uploadedFilesDiv.add(fileInfoDiv);
        }
    }

    private StreamResource getDownloadLink(Document file) {
        byte[] documentData = file.getDocumentData();
        String fileName = file.getFileName();


        StreamResource streamResource = new StreamResource(fileName, () -> new ByteArrayInputStream(documentData));

        streamResource.setContentType("application/octet-stream");
        streamResource.setCacheTime(0);

        return streamResource;
    }

}
