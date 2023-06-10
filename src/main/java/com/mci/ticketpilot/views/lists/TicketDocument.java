package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.service.PilotService;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import java.io.InputStream;

public class TicketDocument extends HorizontalLayout {
    private PilotService service;
    private Ticket currentTicket;
    MemoryBuffer buffer = new MemoryBuffer();

    Upload upload = new Upload(buffer);

    public TicketDocument(PilotService service,Ticket ticket) {
        this.service = service;
        this.currentTicket = ticket;

        upload.addSucceededListener(event -> {
            InputStream inputStream = buffer.getInputStream();

            service.handleUploadedFile(inputStream, event.getFileName());

            Notification.show("Upload succeeded!", 2000, Notification.Position.MIDDLE);
        });

        upload.addFailedListener(event -> {
            Notification.show("Upload failed!", 2000, Notification.Position.MIDDLE);
        });

        add(upload);
    }

}
