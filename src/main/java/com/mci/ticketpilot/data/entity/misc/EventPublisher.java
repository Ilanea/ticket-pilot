package com.mci.ticketpilot.data.entity.misc;

import com.mci.ticketpilot.data.entity.Ticket;
import org.springframework.context.ApplicationEvent;

public class EventPublisher extends ApplicationEvent {
    private Ticket ticket;

    public EventPublisher(Object source, Ticket ticket) {
        super(source);
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}