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
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.charts.model.style.Style;


import java.io.ByteArrayInputStream;
import java.util.List;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.Axis;
import com.vaadin.flow.component.charts.model.ChartType;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Ticket Pilot")
public class DashboardView extends VerticalLayout {
    private final PilotService service;
    private List<Ticket> tickets;
    private List<Project> projects;

    private TicketForm form;
    private Chart statusPieChart;
    private Chart ticketsByUserBarChart;

    public DashboardView(PilotService service) {
        this.service = service;
        this.tickets = service.getUserTickets();

        initCharts();


        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(createBoardStats());
        add(createUserBoard());
        add(createDownloadButton());
    }

    private void initCharts() {
        statusPieChart = createStatusPieChart();
        ticketsByUserBarChart = createTicketsByUserBarChart();
    }

    private void updateCharts() {
        remove(statusPieChart);
        remove(ticketsByUserBarChart);
        statusPieChart = createStatusPieChart();
        ticketsByUserBarChart = createTicketsByUserBarChart();
        add(statusPieChart);
        add(ticketsByUserBarChart);
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
        List<Ticket> userTickets = service.getUserTickets();
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
        userLayout.add(new H1("Current User Projects and Tickets"));

        List<Project> userProjects = service.getUserProjects();
        List<Ticket> userTickets = service.getUserTickets();

        VerticalLayout projectLayout = new VerticalLayout();
        projectLayout.add(new H2("Projects:"));
        for (Project project : userProjects) {
            projectLayout.add(new Span(project.getProjectName()));
        }

        VerticalLayout ticketLayout = new VerticalLayout();
        ticketLayout.add(new H2("Tickets:"));
        for (Ticket ticket : userTickets) {
            ticketLayout.add(new Span(ticket.getTicketName()));
        }

        userLayout.add(projectLayout, ticketLayout);

        userLayout.add(statusPieChart);
        userLayout.add(ticketsByUserBarChart);

        return userLayout;
    }

    private Chart createStatusPieChart() {
        int openCount = 0;
        int inProgressCount = 0;
        int resolvedCount = 0;
        int closedCount = 0;
        int reopenedCount = 0;
        int onHoldCount = 0;

        for (Ticket ticket : tickets) {
            switch (ticket.getTicketStatus()) {
                case OPEN:
                    openCount++;
                    break;
                case IN_PROGRESS:
                    inProgressCount++;
                    break;
                case RESOLVED:
                    resolvedCount++;
                    break;
                case CLOSED:
                    closedCount++;
                    break;
                case REOPENED:
                    reopenedCount++;
                    break;
                case ON_HOLD:
                    onHoldCount++;
                    break;
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
    private Chart createTicketsByUserBarChart() {
        DataSeries series = new DataSeries();
        for (Ticket ticket : tickets) {
            String userName = ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName();
            series.add(new DataSeriesItem(userName, 1));
        }

        Chart chart = new Chart(ChartType.BAR);
        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Tickets by User");
        configuration.getChart().setBackgroundColor(new SolidColor(20, 48, 72));

        // Set the background color of the plot area
        configuration.getChart().setPlotBackgroundColor(new SolidColor(20, 48, 72));

        XAxis xAxis = new XAxis();
        xAxis.setType(AxisType.CATEGORY);
        configuration.addxAxis(xAxis);

        configuration.getyAxis().setTitle("Ticket Count");

        configuration.setSeries(series);

        return chart;
    }
}

