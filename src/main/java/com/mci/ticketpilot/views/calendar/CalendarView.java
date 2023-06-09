package com.mci.ticketpilot.views.calendar;

import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.TicketPriority;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.MainLayout;
import com.mci.ticketpilot.views.lists.TicketForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
import org.vaadin.stefan.fullcalendar.DisplayMode;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;


@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "calendar", layout = MainLayout.class)
@PageTitle("Calendar | Ticket Pilot")
public class CalendarView extends VerticalLayout {

    private PilotService service;
    private FullCalendar calendar;
    private Button prevMonthButton;
    private Button nextMonthButton;
    private Button thisMonthButton;
    private Button todayButton;
    private LocalDate currentDate = LocalDate.now();
    private TicketForm ticketForm;
    private Dialog dialog;

    public CalendarView(PilotService service) {
        this.service = service;

        addClassName("calendar-view");
        setAlignItems(FlexComponent.Alignment.CENTER);
        setSizeFull();

        dialog = new Dialog();
        ticketForm = new TicketForm(service.findAllTickets(), service);

        prevMonthButton = new Button(getPreviousMonthName(), new Icon(VaadinIcon.ARROW_LEFT));
        prevMonthButton.addClickListener(event -> previousMonth());

        nextMonthButton = new Button(getNextMonthName(), new Icon(VaadinIcon.ARROW_RIGHT));
        nextMonthButton.setIconAfterText(true);
        nextMonthButton.addClickListener(event -> nextMonth());

        thisMonthButton = new Button(getcurrentMonth(), new Icon(VaadinIcon.CALENDAR));
        //thisMonthButton.setEnabled(false);

        todayButton = new Button("Today");
        todayButton.addClickListener(event -> goToToday());

        HorizontalLayout headerLayout = new HorizontalLayout(prevMonthButton, thisMonthButton, todayButton, nextMonthButton);
        headerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        add(headerLayout);

        calendar = FullCalendarBuilder.create().withAutoBrowserTimezone().build();
        calendar.addClassName("calendar");
        calendar.setLocale(Locale.ENGLISH);
        calendar.setFirstDay(DayOfWeek.MONDAY);
        calendar.setMaxEntriesPerDay(3);
        calendar.setSizeFull();

        calendar.addDatesRenderedListener(event -> {
            currentDate = event.getIntervalStart();
            prevMonthButton.setText(getPreviousMonthName());
            nextMonthButton.setText(getNextMonthName());
            thisMonthButton.setText(getcurrentMonth());
        });

        calendar.addEntryClickedListener(event -> {
            Entry clickedEntry = event.getEntry();
            Ticket ticket = findTicketByEntry(clickedEntry);
            if (ticket != null) {
                openTicketForm(ticket);
            }
        });

        add(calendar);

        updateCalendar();
    }

    private void updateCalendar() {

        List<Ticket> tickets = service.findAllTickets();
        List<Entry> entries = new ArrayList<>();

        for (Ticket ticket : tickets) {
            if(ticket.getDueDate() != null){
                if(SecurityUtils.userHasAdminRole() || SecurityUtils.userHasManagerRole()){
                    // Admins and Managers can see all tickets
                    Entry entry = new Entry();
                    entry.setAllDay(true);
                    entry.setDisplayMode(DisplayMode.AUTO);
                    entry.setStartEditable(false);
                    entry.setTitle(ticket.getTicketName());
                    entry.setStart(ticket.getTicketCreationDate());
                    entry.setEnd(ticket.getDueDate());
                    entry.setColor(getColor(ticket));
                    entries.add(entry);
                } else if (service.isCurrentUserManager(ticket.getProject()) || service.isCurrentUserAssignee(ticket)){
                    // Managers can see all tickets in their projects and Users can see their own tickets
                    Entry entry = new Entry();
                    entry.setAllDay(true);
                    entry.setDisplayMode(DisplayMode.AUTO);
                    entry.setStartEditable(false);
                    entry.setTitle(ticket.getTicketName());
                    entry.setStart(ticket.getTicketCreationDate());
                    entry.setEnd(ticket.getDueDate());
                    entry.setColor(getColor(ticket));
                    entries.add(entry);
                }
            }
        }

        calendar.getEntryProvider().asInMemory().removeAllEntries();
        calendar.getEntryProvider().asInMemory().addEntries(entries);
    }

    private Ticket findTicketByEntry(Entry entry) {
        List<Ticket> tickets = service.findAllTickets();
        for (Ticket ticket : tickets) {
            if (ticket.getTicketName().equals(entry.getTitle()) && ticket.getTicketCreationDate().equals(entry.getStart().toLocalDate()) && ticket.getDueDate().equals(entry.getEnd().toLocalDate())) {
                return ticket;
            }
        }
        return null;
    }

    private void openTicketForm(Ticket ticket) {
        ticketForm.setTicket(ticket, false);
        dialog.add(ticketForm);

        // Save, Delete and Cancel Listeners kinda not working
        ticketForm.setEnabled(false);

        Button closeButton = new Button("Close", event -> dialog.close());
        dialog.open();
        dialog.removeAll();
        dialog.add(ticketForm, closeButton);
    }

    private void previousMonth() {
        calendar.previous();
    }

    private void nextMonth() {
        calendar.next();
    }

    private void goToToday() {
        calendar.today();
    }

    private String getcurrentMonth() {
        LocalDate today = currentDate;
        String month = today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String year = today.getYear() + "";
        return month + " " + year;
    }

    private String getPreviousMonthName() {
        LocalDate previousMonth = currentDate.minusMonths(1);
        String month = previousMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String year = previousMonth.getYear() + "";
        return month + " " + year;
    }

    private String getNextMonthName() {
        LocalDate nextMonth = currentDate.plusMonths(1);
        String month = nextMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String year = nextMonth.getYear() + "";
        return month + " " + year;
    }

    private String getColor(Ticket ticket){
        String color = "blue";

        switch (ticket.getTicketPriority()) {
            case LOW:
                color = "green";
                break;
            case MEDIUM:
                color = "orange";
                break;
            case HIGH:
                color = "red";
                break;
        }
      
        return color;
    }
}

