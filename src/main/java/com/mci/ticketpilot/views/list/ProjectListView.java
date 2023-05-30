package com.mci.ticketpilot.views.list;

import com.mci.ticketpilot.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "projects", layout = MainLayout.class)
@PageTitle("Projects | Ticket Pilot")
public class ProjectListView extends VerticalLayout {
}
