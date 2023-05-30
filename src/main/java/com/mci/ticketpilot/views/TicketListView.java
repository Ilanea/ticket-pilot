package com.mci.ticketpilot.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "tickets", layout = MainLayout.class)
@PageTitle("Tickets | Ticket Pilot")
public class TicketListView extends VerticalLayout {
}
