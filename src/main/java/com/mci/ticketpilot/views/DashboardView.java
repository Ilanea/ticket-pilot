package com.mci.ticketpilot.views;

import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.service.PdfService;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.views.lists.TicketForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.charts.model.style.Style;
import java.time.Year;



import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;


import java.util.stream.Collectors;
import java.util.stream.IntStream;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.XAxis;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Ticket Pilot")
public class DashboardView extends VerticalLayout {
    private final PilotService service;
    private List<Ticket> tickets;
    private List<Project> projects;
    private DatePicker fromFilter;
    private DatePicker toFilter;
    private LocalDate fromDate = LocalDate.now();
    private LocalDate toDate = LocalDate.now();


    private TicketForm form;
    private Chart statusPieChart;
    private Chart createTicketsbyDayBarChart;

    private Chart createTicketsByMonthLineChart;

    private Board boardChart;

    private Chart createTicketsByUserBarChart;

    private VerticalLayout userLayout = new VerticalLayout();
    private Component oldUserlayout;
    private Button applyFilterButton;

    @Autowired
    public DashboardView(PilotService service) {
        this.service = service;
        // Initialize the DatePicker
        this.fromFilter = new DatePicker();
        this.fromFilter.setLabel("From date");
        this.fromFilter.setValue(LocalDate.now());
        // Do the same for toDate if it's not initialized yet
        this.toFilter = new DatePicker();
        this.toFilter.setLabel("To date");
        this.toFilter.setValue(LocalDate.now());


        initCharts();

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);


        add(createBoardStats(), createUserBoard());
    }



    private void initCharts() {
        createTicketsbyDayBarChart = createTicketsbyDayBarChart();
        createTicketsByMonthLineChart = createTicketsByMonthLineChart(); // add this line
        statusPieChart = createStatusPieChart();
        createTicketsByUserBarChart = createTicketsByUserBarChart();

        fromDate = fromFilter.getValue();
        toDate = toFilter.getValue();

        statusPieChart.setHeight("400px");
        statusPieChart.setWidth("850px");
        createTicketsbyDayBarChart.setHeight("400px");
        createTicketsbyDayBarChart.setWidth("850px");
        createTicketsByUserBarChart.setHeight("400px");
        createTicketsByUserBarChart.setWidth("850px");
        createTicketsByMonthLineChart.setHeight("400px"); // add this line
        createTicketsByMonthLineChart.setWidth("850px"); // add this line
    }

    private void updateContent() {
        this.tickets = service.getTicketsperDate(fromDate, toDate);
        initCharts();
        createUserBoard();
    }

    private Component createDownloadButton() {
        Button downloadButton = new Button("Download as PDF");
        List<Project> userProjects = service.getUserProjects();
        List<Ticket> userTickets = service.getTicketsperDate(fromDate, toDate);
        byte[] pdfBytes = new PdfService().createPdf(userProjects, userTickets);
        StreamResource pdfResource = new StreamResource("dashboard.pdf", () -> new ByteArrayInputStream(pdfBytes));
        Anchor downloadLink = new Anchor(pdfResource, "");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(downloadButton);
        return downloadLink;
    }



    private Component getUserStats() {
        Span stats = new Span(service.countUsers() + " contacts");
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Component getTicketStats() {
        Span stats = new Span(service.countTickets() + " tickets");
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Component getProjectStats() {
        Span stats = new Span(service.countProjects() + " projects");
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Component createBoardStats() {
        Row rootRow = new Row();
        rootRow.add(new H1("Stats"), 1);

        Row nestedRow = new Row(
                getUserStats(),
                getTicketStats(),
                getProjectStats());
        rootRow.addNestedRow(nestedRow);

        Board board = new Board();
        board.add(rootRow);

        return board;
    }

    private Component createUserBoard() {
        userLayout.removeAll(); // clears the layout

        HorizontalLayout barLayout = new HorizontalLayout();
        HorizontalLayout chartLayout = new HorizontalLayout();
        HorizontalLayout ChartsTitelLayout = new HorizontalLayout();
        // Move the initialization of the variables before their usage
        fromFilter = new DatePicker();
        fromFilter.setValue(LocalDate.now().minusDays(30));
        fromFilter.setLabel("Filter From Date");
        toFilter = new DatePicker();
        toFilter.setValue(LocalDate.now().plusDays(1));
        toFilter.setLabel("Filter To Date");

        applyFilterButton = new Button("Apply Filter");
        applyFilterButton.addClassName("applybutton-class");
        applyFilterButton.addClickListener(event -> {
            fromDate = fromFilter.getValue();
            toDate = toFilter.getValue();
            updateContent();
        });
        userLayout.add(new H1("Charts"));




        HorizontalLayout filters = new HorizontalLayout();
        filters.addClassName("filters");
        filters.add(fromFilter, toFilter, applyFilterButton);
        ChartsTitelLayout.add(filters);

        chartLayout.add(createTicketsbyDayBarChart, createTicketsByMonthLineChart);
        barLayout.add(statusPieChart, createTicketsByUserBarChart);

        userLayout.add(ChartsTitelLayout, chartLayout , barLayout);

        add(userLayout); // add userLayout to the current layout


        return userLayout;
    }

    private Chart createStatusPieChart() {
        int openCount = 0;
        int inProgressCount = 0;
        int resolvedCount = 0;
        int closedCount = 0;
        int reopenedCount = 0;
        int onHoldCount = 0;

        for (Ticket ticket : service.getTicketsperDate(fromDate, toDate)) {
            switch (ticket.getTicketStatus()) {
                case OPEN -> openCount++;
                case IN_PROGRESS -> inProgressCount++;
                case RESOLVED -> resolvedCount++;
                case CLOSED -> closedCount++;
                case REOPENED -> reopenedCount++;
                case ON_HOLD -> onHoldCount++;
            }
        }
        Style titleStyle = new Style();
        titleStyle.setColor(new SolidColor("#FFFFFF")); // Set color to white
        titleStyle.setFontSize("24px");

        Style axisLabelStyle = new Style();
        axisLabelStyle.setColor(new SolidColor("#FFFFFF")); // Set color to white

        Chart chart = new Chart(ChartType.PIE);
        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Ticket Status");
        configuration.getChart().setBackgroundColor(new SolidColor(20, 48, 72));
        configuration.getChart().setBorderRadius(10);
        configuration.getChart().setBorderColor(new SolidColor(20, 48, 72));
        configuration.getChart().setBorderWidth(5);
        configuration.getTitle().setStyle(titleStyle);
        configuration.getxAxis().getLabels().setStyle(axisLabelStyle);


        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("OPEN", openCount));
        series.add(new DataSeriesItem("In Progress", inProgressCount));
        series.add(new DataSeriesItem("Resolved", resolvedCount));
        series.add(new DataSeriesItem("Closed", closedCount));
        series.add(new DataSeriesItem("Reopened", reopenedCount));
        series.add(new DataSeriesItem("On Hold", onHoldCount));

// Add the data series to the chart configuration
        configuration.setSeries(series);

        Div chartContainer = new Div();
        chartContainer.getStyle().set("width", "500px"); // Set the desired width


// Return the chart
        return chart;
    }

    // ...
    private Chart createTicketsbyDayBarChart() {
        Map<LocalDate, Long> ticketCountByDay = getTicketCountsByDay(service.getTicketsperDate(fromDate, toDate));
        DataSeries series = new DataSeries();
        int daysBetween = (int) (toDate.toEpochDay() - fromDate.toEpochDay());
        for (int i = 0; i <= daysBetween; i++) {
            LocalDate date = fromDate.plusDays(i);
            series.add(new DataSeriesItem(date.toString(), ticketCountByDay.getOrDefault(date, 0L)));
        }

        series.setName(String.format("Tickets from %s to %s", fromDate, toDate));

        Chart chart = new Chart(ChartType.COLUMN);
        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Tickets by Day for Selected Date Range");
        Style titleStyle = new Style();
        titleStyle.setColor(new SolidColor("#FFFFFF")); // Set color to white
        titleStyle.setFontSize("24px");
        configuration.getTitle().setStyle(titleStyle);
        configuration.getChart().setBackgroundColor(new SolidColor(20, 48, 72));

        // Set the background color of the plot area
        configuration.getChart().setPlotBackgroundColor(new SolidColor(20, 48, 72));

        XAxis xAxis = new XAxis();
        xAxis.setCategories(
                IntStream.rangeClosed(0, daysBetween)
                        .mapToObj(i -> fromDate.plusDays(i).toString())
                        .toArray(String[]::new)
        );
        configuration.addxAxis(xAxis);

        // remaining code...

        configuration.setSeries(series);

        return chart;
    }

    private Chart createTicketsByUserBarChart() {
        Map<String, Integer> ticketCountByUser = new HashMap<>();
        for (Ticket ticket : service.getTicketsperDate(fromDate, toDate)) {
            ticketCountByUser.put(ticket.getAssignee().toString(), ticketCountByUser.getOrDefault(ticket.getAssignee().toString(), 0) + 1);
        }

        DataSeries series = new DataSeries();
        for (Map.Entry<String, Integer> entry : ticketCountByUser.entrySet()) {
            DataSeriesItem item = new DataSeriesItem(entry.getKey(), entry.getValue());
            item.setColor(random()); // Assign a random color
            series.add(item);
        }

        Chart chart = new Chart(ChartType.COLUMN);
        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Tickets by User");
        Style titleStyle = new Style();
        titleStyle.setColor(new SolidColor("#FFFFFF")); // Set color to white
        titleStyle.setFontSize("24px");
        configuration.getTitle().setStyle(titleStyle);
        configuration.getChart().setBackgroundColor(new SolidColor(20, 48, 72));

        // Set the background color of the plot area
        configuration.getChart().setPlotBackgroundColor(new SolidColor(20, 48, 72));

        XAxis xAxis = new XAxis();
        xAxis.setCategories(ticketCountByUser.keySet().toArray(new String[0]));
        configuration.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Ticket Count");
        yAxis.setTickInterval(1);  // Set tick interval to 1
        configuration.addyAxis(yAxis);

        configuration.setSeries(series);

        return chart;
    }



    private Board createProjectAndTicketBoard() {
        Board board = new Board();

        // First row for projects
        Div projectHeader = new Div();
        projectHeader.setText("Projects");
        projectHeader.addClassName("board-header");
        Div projectContent = new Div();
        for (Project project : service.getUserProjects()) {
            Span projectName = new Span(project.getProjectName());
            projectContent.add(projectName);
        }

        Row projectRow = board.addRow(projectHeader, projectContent);

        // Second row for tickets
        Div ticketHeader = new Div();
        ticketHeader.setText("Tickets");
        ticketHeader.addClassName("board-header");

        Div ticketContent = new Div();
        for (Ticket ticket : service.getTicketsperDate(fromDate, toDate)) {
            Span ticketName = new Span(ticket.getTicketName());
            ticketContent.add(ticketName);
        }

        Row ticketRow = board.addRow(ticketHeader, ticketContent);

        // Style the board
        board.addClassName("project-and-ticket-board");

        return board;
    }

    /**
     * Creates a bar chart showing the number of tickets per day for the current month.
     * @Param fromDate
     * @Param toDate
     * @return
     */

    /**
     * Creates a line chart showing the number of tickets per month for the current year.
     * @return
     */
    private Chart createTicketsByMonthLineChart() {
        // Get dates from the DatePicker components
        LocalDate fromDate = fromFilter.getValue();
        LocalDate toDate = toFilter.getValue();

        // If fromDate or toDate is null, set them to defaults (optional)
        if (fromDate == null) {
            fromDate = LocalDate.of(Year.now().getValue(), 1, 1); // start of current year
        }
        if (toDate == null) {
            toDate = LocalDate.now(); // current date
        }

        Map<Month, Long> ticketCountByMonth = getTicketCountsByMonth(service.getTicketsperDate(fromDate, toDate));

        DataSeries series = new DataSeries();
        LocalDate dateIterator = fromDate;
        while (!dateIterator.isAfter(toDate)) {
            Month currentMonth = dateIterator.getMonth();
            series.add(new DataSeriesItem(currentMonth.name(), ticketCountByMonth.getOrDefault(currentMonth, 0L)));
            dateIterator = dateIterator.plusMonths(1);
        }

        // set the series name to be the year range
        series.setName(fromDate.getYear() + " - " + toDate.getYear());

        Chart chart = new Chart(ChartType.LINE);
        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Tickets by Month");
        Style titleStyle = new Style();
        titleStyle.setColor(new SolidColor("#FFFFFF")); // Set color to white
        titleStyle.setFontSize("24px");
        configuration.getTitle().setStyle(titleStyle);
        configuration.getChart().setBackgroundColor(new SolidColor(20, 48, 72));

        // Set the background color of the plot area
        configuration.getChart().setPlotBackgroundColor(new SolidColor(20, 48, 72));

        XAxis xAxis = new XAxis();
        // Get unique months within the date range in the correct order
        List<String> monthNames = new ArrayList<>();
        dateIterator = fromDate;
        while (!dateIterator.isAfter(toDate)) {
            monthNames.add(dateIterator.getMonth().name());
            dateIterator = dateIterator.plusMonths(1);
        }
        xAxis.setCategories(monthNames.toArray(new String[0]));
        configuration.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Ticket Count");
        configuration.addyAxis(yAxis);

        configuration.setSeries(series);

        return chart;
    }






    public static SolidColor random() {
        return new SolidColor(
                (int) (Math.random() * 256),  // Red
                (int) (Math.random() * 256),  // Green
                (int) (Math.random() * 256)   // Blue
        );
    }

    private Map<LocalDate, Long> getTicketCountsByDay(List<Ticket> tickets) {
        if (tickets != null) {
            return tickets.stream()
                    .filter(ticket -> {
                        LocalDate ticketDate = ticket.getTicketCreationDate();
                        return ticketDate.getMonth().equals(LocalDate.now().getMonth())
                                && ticketDate.getYear() == LocalDate.now().getYear();
                    })
                    .collect(Collectors.groupingBy(
                            Ticket::getTicketCreationDate,
                            Collectors.counting()
                    ));
        } else {
            return new HashMap<>();
        }
    }

    private Map<Month, Long> getTicketCountsByMonth(List<Ticket> tickets) {
        if (tickets != null) {
            return tickets.stream()
                    .filter(ticket -> ticket.getTicketCreationDate().getYear() == LocalDate.now().getYear())
                    .collect(Collectors.groupingBy(
                            ticket -> ticket.getTicketCreationDate().getMonth(),
                            Collectors.counting()
                    ));
        } else {
            return new HashMap<>();
        }
    }

}


