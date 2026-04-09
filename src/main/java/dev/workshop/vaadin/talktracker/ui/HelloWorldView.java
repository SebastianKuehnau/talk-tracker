package dev.workshop.vaadin.talktracker.ui;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class HelloWorldView extends HorizontalLayout {

    public HelloWorldView() {
        var nameField = new TextField("name");
        nameField.setAutofocus(true);
        var showButton = new Button("Show");
        showButton.addClickListener(event ->
                Notification
                        .show("Hello " + nameField.getValue())
                        .addThemeVariants(NotificationVariant.SUCCESS));
        showButton.addClickShortcut(Key.ENTER);
        showButton.addThemeVariants(ButtonVariant.PRIMARY);

        add(nameField, showButton);
        setAlignItems(Alignment.BASELINE);
        setPadding(true);
    }
}
