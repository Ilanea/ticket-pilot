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


import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.server.StreamResource;
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
    private LocalDate fromDate;
    private LocalDate toDate;


    private TicketForm form;
    private Chart statusPieChart;
    private Chart ticketsByUserBarChart;

    private Board boardChart;

    private Chart createTicketsbyDayBarChart;

    private Component userLayout;
    private Button applyFilterButton;

    @Autowired
    public DashboardView(PilotService service) {
        this.service = service;

        initCharts();

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);


        add(createBoardStats(), createUserBoard(),createDownloadButton());
    }



    private void initCharts() {
        statusPieChart = createStatusPieChart();
        ticketsByUserBarChart = createTicketsByUserBarChart();
        createTicketsbyDayBarChart = createTicketsByDayBarChart();

        statusPieChart.setHeight("400px");
        statusPieChart.setWidth("850px");
        ticketsByUserBarChart.setHeight("400px");
        ticketsByUserBarChart.setWidth("850px");
        createTicketsbyDayBarChart.setHeight("400px");
        createTicketsbyDayBarChart.setWidth("850px");
    }

    private void updateUserBoard() {
        remove(userLayout);
        userLayout = createUserBoard();
        add(userLayout);
    }

    private void updateContent() {
        this.tickets = service.getTicketsperDate(fromDate, toDate);
        updateCharts();
        updateUserBoard();
    }


    private VerticalLayout chartLayout; // Assuming you are adding charts to this layout

    // Update charts method
    private void updateCharts() {
        // Create new charts
        Chart newStatusPieChart = createStatusPieChart();
        Chart newTicketsByUserBarChart = createTicketsByUserBarChart();
        Chart newCreateTicketsbyDayBarChart = createTicketsByDayBarChart();


        // Remove old charts
        chartLayout.remove(statusPieChart, ticketsByUserBarChart, boardChart, createTicketsbyDayBarChart);

        // Replace old charts with new ones
        chartLayout.add(newStatusPieChart, newTicketsByUserBarChart, newCreateTicketsbyDayBarChart);

        // Update reference to charts
        statusPieChart = newStatusPieChart;
        ticketsByUserBarChart = newTicketsByUserBarChart;
        createTicketsbyDayBarChart = newCreateTicketsbyDayBarChart;
    }

    private void refreshUserBoard() {
        remove(form);
        form = new TicketForm(tickets, service);
        form.setSaveListener(form.addSaveListener(event -> {
            Ticket newTicket = event.getTicket();
            tickets.add(newTicket);
            refreshUserBoard();
            updateCharts();
        }));
        add(form);
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
        VerticalLayout userLayout = new VerticalLayout();
        HorizontalLayout barLayout = new HorizontalLayout();
        HorizontalLayout chartLayout = new HorizontalLayout();
        HorizontalLayout ChartsTitelLayout = new HorizontalLayout();
        // Move the initialization of the variables before their usage
        fromFilter = new DatePicker();
        fromFilter.setLabel("Filter From Date");
        toFilter = new DatePicker();
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
        ChartsTitelLayout.add(new H2("Filter:"));
        ChartsTitelLayout.add(filters);



        barLayout.add(statusPieChart);
        barLayout.add(ticketsByUserBarChart);
//chartLayout.add(boardChart);
chartLayout.add(createTicketsbyDayBarChart);


        userLayout.add(ChartsTitelLayout, chartLayout , barLayout);

        return userLayout;
    }

    private Chart createStatusPieChart() {
        int openCount = 0;
        int inProgressCount = 0;
        int resolvedCount = 0;
        int closedCount = 0;
        int reopenedCount = 0;
        int onHoldCount = 0;
        int anythingelse = 1;

        for (Ticket ticket : service.findAllTickets()) {
            switch (ticket.getTicketStatus()) {
                case OPEN -> openCount++;
                case IN_PROGRESS -> inProgressCount++;
                case RESOLVED -> resolvedCount++;
                case CLOSED -> closedCount++;
                case REOPENED -> reopenedCount++;
                case ON_HOLD -> onHoldCount++;
                default -> anythingelse++;
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
    private Chart createTicketsByUserBarChart() {
        Map<String, Integer> ticketCountByUser = new HashMap<>();
        for (Ticket ticket : service.findAllTickets()) {
            String userName = ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName();
            ticketCountByUser.put(userName, ticketCountByUser.getOrDefault(userName, 0) + 1);
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

    private Chart createTicketsByDayBarChart() {
        Map<LocalDate, Long> ticketCountByDay = getTicketCountsByDay(service.getTicketsperDate(fromDate, toDate));

        DataSeries series = new DataSeries();
        LocalDate now = LocalDate.now();
        int daysInMonth = now.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate date = now.withDayOfMonth(i);
            series.add(new DataSeriesItem(date.toString(), ticketCountByDay.getOrDefault(date, 0L)));
        }

        // set the series name to be the current month
        series.setName(Month.of(now.getMonthValue()).name());

        Chart chart = new Chart(ChartType.COLUMN);
        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Tickets by Day for Current Month");
        Style titleStyle = new Style();
        titleStyle.setColor(new SolidColor("#FFFFFF")); // Set color to white
        titleStyle.setFontSize("24px");
        configuration.getTitle().setStyle(titleStyle);
        configuration.getChart().setBackgroundColor(new SolidColor(20, 48, 72));

        // Set the background color of the plot area
        configuration.getChart().setPlotBackgroundColor(new SolidColor(20, 48, 72));
        // Set title style, chart colors, etc here as per your existing code...

        XAxis xAxis = new XAxis();
        xAxis.setCategories(IntStream.rangeClosed(1, daysInMonth).mapToObj(Integer::toString).toArray(String[]::new));
        configuration.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Ticket Count");
        yAxis.setTickInterval(1);  // Set tick interval to 1

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
}


