package com.mci.ticketpilot.views.calendar;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.TicketPriority;
import com.mci.ticketpilot.data.entity.TicketStatus;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.views.MainLayout;
import com.mci.ticketpilot.views.lists.TicketListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "calendar", layout = MainLayout.class)
@PageTitle("Calendar | Ticket Pilot")
public class CalendarView extends VerticalLayout{

    private PilotService service;
    FullCalendar calendar;

    public CalendarView(PilotService service) {
        this.service = service;

        addClassName("calendar-view");
        setSizeFull();

        this.calendar = getCalendar();

        add(calendar);

    }

    private FullCalendar getCalendar() {

        FullCalendar calendar = FullCalendarBuilder.create().withAutoBrowserTimezone().build();
        calendar.addClassName("calendar");
        calendar.setLocale(Locale.ENGLISH);
        calendar.setFirstDay(DayOfWeek.MONDAY);
        calendar.setEditable(false);
        calendar.setDragScrollActive(false);
        calendar.setSizeFull();

        List<Ticket> tickets = service.findAllTickets();
        List<Entry> entries = new ArrayList<>();


        for (Ticket ticket : tickets) {
            if(ticket.getDueDate() != null){
                Entry entry = new Entry();
                entry.setTitle(ticket.getTicketName());
                entry.setStart(ticket.getTicketCreationDate());
                entry.setEnd(ticket.getDueDate());
                entry.setColor(getColor(ticket));
                entry.setEditable(false);
                entries.add(entry);
            }
        }

        calendar.getEntryProvider().asInMemory().addEntries(entries);

        return calendar;
    }

    private String getColor(Ticket ticket){
        String color = "blue";

        TicketPriority priority = ticket.getTicketPriority();
        if (priority == TicketPriority.DEFAULT) {
            color = "green";
        } else if (priority == TicketPriority.LOW) {
            color = "yellow";
        } else if (priority == TicketPriority.MEDIUM) {
            color = "orange";
        } else if (priority == TicketPriority.HIGH) {
            color = "red";
        }

        return color;
    }

}

