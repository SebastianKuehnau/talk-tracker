package dev.workshop.vaadin.talktracker.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;


@Route("")
public class HelloWorldView extends VerticalLayout {

    public HelloWorldView() {
        var nameField = new TextField("Name");

        var showButton  = new Button("Show");
        showButton.addThemeVariants(ButtonVariant.PRIMARY);
        showButton.addClickListener(e -> {
            Notification.show("Hello " + nameField.getValue() + "!");
        });

        var layout = new HorizontalLayout(nameField, showButton);
        layout.setAlignItems(Alignment.BASELINE);
        add(layout);
    }
}
