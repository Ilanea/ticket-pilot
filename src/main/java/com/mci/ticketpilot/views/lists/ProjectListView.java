package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.MainLayout;
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
    private Grid<Project> grid = new Grid<>(Project.class);
    private TextField filterText = new TextField();
    private ProjectForm form;
    private PilotService service;
    private Div gridContainer;
    private Div formContainer;
    private Button createProjectButton;
    private Button backButton;


    public ProjectListView(PilotService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureForm();
        configureGrid();

        add(getToolbar(), getGridContainer(), getFormContainer());
        updateList();
        closeEditor();
    }

    private Div getGridContainer() {
        gridContainer = new Div(grid);
        gridContainer.addClassName("grid-container");
        gridContainer.setSizeFull();
        return gridContainer;
    }

    private Div getFormContainer() {
        formContainer = new Div(form);
        formContainer.addClassName("form-container");
        formContainer.setVisible(false);
        formContainer.setSizeFull();
        return formContainer;
    }

    private void configureForm() {
        form = new ProjectForm(service.findAllProjects(), service);
        form.setSizeFull();
        form.addSaveListener(this::saveProject);
        form.addDeleteListener(this::deleteProject);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveProject(ProjectForm.SaveEvent event) {
        service.saveProject(event.getProject());
        updateList();
        closeEditor();
        // Benachrichtigung erstellen und anzeigen
        Notification notification = new Notification("Neues Projekt wurde erstellt", 3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
    }

    private void deleteProject(ProjectForm.DeleteEvent event) {
        service.deleteProject(event.getProject());
        updateList();
        closeEditor();
        // Benachrichtigung erstellen und anzeigen
        Notification notification = new Notification("Projekt wurde gelöscht", 3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
    }

    private void configureGrid() {
        grid.addClassNames("project-grid");
        grid.setSizeFull();
        grid.setColumns("projectName", "manager");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editProject(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by title...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        this.createProjectButton = new Button("Create Project");
        createProjectButton.addClickListener(click -> createProject());

        this.backButton = new Button("Back to List");
        backButton.addClickListener(click -> closeEditor());
        backButton.setVisible(false);

        var toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        toolbar.add(filterText, backButton);

        // only add Create Project Button when current authenticated User has MANAGER or ADMIN role
        if (SecurityUtils.userHasManagerRole() || SecurityUtils.userHasAdminRole()) {
            toolbar.add(createProjectButton);
        }

        return toolbar;
    }

    public void editProject(Project project) {
        if (project == null) {
            closeEditor();
        } else {
            form.setProject(project);
            gridContainer.setVisible(false);
            formContainer.setVisible(true);
            createProjectButton.setVisible(false);
            backButton.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setProject(null);
        gridContainer.setVisible(true);
        formContainer.setVisible(false);
        createProjectButton.setVisible(true);
        backButton.setVisible(false);
        removeClassName("editing");
    }

    private void createProject() {
        grid.asSingleSelect().clear();
        formContainer.setVisible(true);
        gridContainer.setVisible(false);
        createProjectButton.setVisible(false);
        backButton.setVisible(true);
        editProject(new Project());
    }

    private void updateList() {
        grid.setItems(service.findAllProjects(filterText.getValue()));
    }
}