package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.UserRole;
import com.mci.ticketpilot.data.entity.Users;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;


import java.util.List;

public class UserForm extends FormLayout {
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    ComboBox<UserRole> userRole = new ComboBox<>("Role");
    // Does not work, only shows BCrypt encrypted passwords and takes BCrypt encrypted Passwords for them to work
    PasswordField password = new PasswordField("Password");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Users> binder = new BeanValidationBinder<>(Users.class);

    public UserForm(List<Users> users) {
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        userRole.setItems(UserRole.values());
        //userRole.setReadOnly(true);

        password.setRevealButtonVisible(false);

        if (!users.isEmpty()) {

            add(firstName,
                    lastName,
                    email,
                    userRole,
                    password,
                    createButtonsLayout());
        }
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    public void setUser(Users user) { binder.setBean(user); }

    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<UserForm> {
        private Users user;

        protected ContactFormEvent(UserForm source, Users user) {
            super(source, false);
            this.user = user;
        }

        public Users getUser() {
            return user;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(UserForm source, Users user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(UserForm source, Users user) {
            super(source, user);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

}
