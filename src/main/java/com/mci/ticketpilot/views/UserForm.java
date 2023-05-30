package com.mci.ticketpilot.views;

import com.mci.ticketpilot.data.entity.UserRole;
import com.mci.ticketpilot.data.entity.Users;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.shared.Registration;
import io.beanmother.core.converter.std.StringToEnumConverter;

import java.util.List;

public class UserForm extends FormLayout {
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    //TextField userRole = new TextField("Role");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Users> binder = new BeanValidationBinder<>(Users.class);

    public UserForm(List<Users> users) {
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        if (!users.isEmpty()) {

            // Convert Role as Enum to String for TextField
            //binder.forField(userRole)
            //        .withConverter(toString())
            //        .bind(Users::getUserRole, Users::setUserRole);

            add(firstName,
                    lastName,
                    email,
                    //userRole,
                    createButtonsLayout());
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
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
