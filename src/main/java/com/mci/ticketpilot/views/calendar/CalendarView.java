package com.mci.ticketpilot.views.calendar;

import com.mci.ticketpilot.data.entity.Ticket;
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

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "calendar", layout = MainLayout.class)
@PageTitle("Calendar | Ticket Pilot")
public class CalendarView extends VerticalLayout{

    private PilotService service;
    private Map<String, VerticalLayout> dates;
    private Select<String> monthPicker;
    private Select<Integer> yearPicker;
    private VerticalLayout calendarLayout; // New field to hold calendar

    public CalendarView(PilotService service) {
        this.service = service;

        addClassName("calendar-view");
        setSizeFull();
        this.dates = new HashMap<>();

        monthPicker = new Select<>();
        monthPicker.setItems("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        monthPicker.setValue("January");
        monthPicker.addValueChangeListener(event -> createCalendar());

        yearPicker = new Select<>();
        yearPicker.setItems(getYears());
        yearPicker.setValue(YearMonth.now().getYear());
        yearPicker.addValueChangeListener(event -> createCalendar());

        HorizontalLayout pickerLayout = new HorizontalLayout(monthPicker, yearPicker);
        add(pickerLayout);

        calendarLayout = new VerticalLayout(); // Initialize the calendar layout
        add(calendarLayout); // Add it to the main layout

        createCalendar();
    }

    private void createCalendar() {
        Month selectedMonth = Month.valueOf(monthPicker.getValue().toUpperCase());
        int selectedYear = yearPicker.getValue();
        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
        int daysInMonth = yearMonth.lengthOfMonth();

        calendarLayout.removeAll(); // Clear only the calendar layout

        HorizontalLayout row = new HorizontalLayout();
        for(int i = 1; i <= daysInMonth; i++) {
            VerticalLayout day = createMonthColumn(String.valueOf(i));
            day.addClassName("day");
            day.setWidth("200px");
            day.setHeight("200px");
            dates.put(String.valueOf(i), day);
            row.add(day);

            if(i % 7 == 0) { // Create a new row for every 7 days
                calendarLayout.add(row);
                row = new HorizontalLayout();
            }
        }

        if(daysInMonth % 7 != 0) { // Add the last row if it has less than 7 days
            calendarLayout.add(row);
        }
    }

    private List<Integer> getYears() {
        List<Integer> years = new ArrayList<>();
        int currentYear = YearMonth.now().getYear();
        for (int i = currentYear - 10; i <= currentYear + 10; i++) {
            years.add(i);
        }
        return years;
    }

    private VerticalLayout createMonthColumn(String date) {
        VerticalLayout month = new VerticalLayout();
        month.addClassName("month-column");
        month.setWidth("150px");
        month.setHeight("150px");

        Label label = new Label(date);
        month.add(label);

        return month;
    }

    private void updateView() {
        for (Ticket ticket : service.findAllTickets()) {
            TicketStatus status = ticket.getTicketStatus();
            // Convert the status to a string
            String statusString = status.name();
            VerticalLayout column = dates.get(statusString);

            if (column != null) {
                Component ticketComponent = createTicketComponent(ticket);
                column.add(ticketComponent);
            } else {
                System.out.println("Could not find column for status " + statusString);
            }
        }
    }

    private Component createTicketComponent(Ticket ticket) {
        VerticalLayout ticketDiv = new VerticalLayout();
        ticketDiv.addClassName("ticket");

        Label ticketNameLabel = new Label("Name: " + ticket.getTicketName() + "\n");


        ticketNameLabel.addClassName("ticket-name");


        ticketDiv.add(ticketNameLabel);

        ticketDiv.addClickListener(event -> openTicket());

        return ticketDiv;
    }

    private void openTicket() {
        UI.getCurrent().navigate(TicketListView.class);
    }

}

