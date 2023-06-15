package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Users;
import com.mci.ticketpilot.data.service.PilotService;
import com.mci.ticketpilot.security.SecurityUtils;
import com.mci.ticketpilot.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.notification.Notification;

@SpringComponent
@Scope("prototype")
@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users | Ticket Pilot")
@RolesAllowed({"ADMIN", "MANAGER"})
public class UserListView extends VerticalLayout {
    private Grid<Users> grid = new Grid<>(Users.class);
    private TextField filterText = new TextField();
    private UserForm form;
    private PilotService service;


    public UserListView(PilotService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new UserForm(service.findAllUsers());
        form.setWidth("25em");
        form.addSaveListener(this::saveUser);
        form.addDeleteListener(this::deleteUser);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveUser(UserForm.SaveEvent event) {
        service.saveUser(event.getUser());
        updateList();
        closeEditor();
        // Benachrichtigung erstellen und anzeigen
        Notification notification = new Notification("User saved", 3000);
        notification.setPosition(Notification.Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        service.deleteUser(event.getUser());
        updateList();
        closeEditor();
        // Benachrichtigung erstellen und anzeigen
        Notification notification = new Notification("User deleted", 3000);
        notification.setPosition(Notification.Position.BOTTOM_END);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.open();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email", "userStatus", "userRole");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button createUserButton = new Button("Create User");
        createUserButton.addClickListener(click -> {
            // createUser-Funktion aufrufen
            createUser();

        });
        var toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        toolbar.add(filterText);

        // only add Create User Button when current authenticated User has ADMIN role
        if (SecurityUtils.userHasAdminRole()){
            toolbar.add(createUserButton);
        }

        return toolbar;
    }

    public void editUser(Users user) {
        if (user == null) {
            closeEditor();
        } else {
            form.setUser(user);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void createUser() {
        grid.asSingleSelect().clear();
        editUser(new Users());
    }

    private void updateList() {
        grid.setItems(service.findAllUsers(filterText.getValue()));
    }
}