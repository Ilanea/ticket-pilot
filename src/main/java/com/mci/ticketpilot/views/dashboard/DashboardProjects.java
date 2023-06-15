package com.mci.ticketpilot.views.dashboard;

import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityService;
import com.mci.ticketpilot.views.lists.ProjectForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringComponent
@Scope("prototype")
@PermitAll
public class DashboardProjects extends VerticalLayout {
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private Grid<Project> grid = new Grid<>(Project.class);;
    private TextField filterText = new TextField();
    private ProjectForm form;
    private PilotService service;
    private Div gridContainer;
    private Div formContainer;
    private Button backButton;

    public DashboardProjects(PilotService service) {
        this.service = service;
        addClassName("dashboard-projects-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getGridContainer(), getFormContainer());
        updateList();
        closeEditor();
    }

    private Div getGridContainer() {
        gridContainer = new Div(grid);
        gridContainer.setClassName("dashboard-projects-grid-container");
        gridContainer.setVisible(true);
        gridContainer.setSizeFull();
        return gridContainer;
    }

    private Div getFormContainer() {
        formContainer = new Div(form);
        formContainer.setClassName("dashboard-projects-form-container");
        formContainer.setVisible(false);
        formContainer.setSizeFull();
        return formContainer;
    }

    private void configureForm() {
        form = new ProjectForm(service.findAllProjectsForUser(), service);
        form.addSaveListener(this::saveProject);
        form.addDeleteListener(this::deleteProject);
        form.addCloseListener(e -> closeEditor());
    }

    private void configureGrid() {
        grid.setColumns("projectName", "projectStartDate", "projectEndDate");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editProject(event.getValue()));
    }

    private void saveProject(ProjectForm.SaveEvent event) {
        service.saveProject(event.getProject());
        updateList();
        closeEditor();
        // Benachrichtigung erstellen und anzeigen
        Notification notification = new Notification("Project saved", 3000);
        notification.setPosition(Notification.Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
    }

    private void deleteProject(ProjectForm.DeleteEvent event) {
        if(event.getProject().getTickets().size() > 0) {
            // Benachrichtigung erstellen und anzeigen
            Notification notification = new Notification("Project cannot be deleted", 3000);
            notification.setPosition(Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        } else {
            service.deleteProject(event.getProject());
            updateList();
            closeEditor();
            // Benachrichtigung erstellen und anzeigen
            Notification notification = new Notification("Project deleted", 3000);
            notification.setPosition(Notification.Position.BOTTOM_END);
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            notification.open();
        }
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by title...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        this.backButton = new Button("Back to List");
        backButton.addClickListener(click -> closeEditor());
        backButton.setVisible(false);

        var toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        toolbar.add(filterText, backButton);

        return toolbar;
    }

    public void editProject(Project project) {
        if (project == null) {
            closeEditor();
        } else {
            form.setProject(project);
            filterText.setVisible(false);
            gridContainer.setVisible(false);
            formContainer.setVisible(true);
            backButton.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setProject(null);
        filterText.setVisible(true);
        gridContainer.setVisible(true);
        formContainer.setVisible(false);
        backButton.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllProjectsForUser(filterText.getValue()));
    }
}