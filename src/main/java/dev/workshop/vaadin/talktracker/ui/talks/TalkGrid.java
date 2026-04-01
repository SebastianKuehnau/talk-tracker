package dev.workshop.vaadin.talktracker.ui.talks;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.signals.shared.SharedNumberSignal;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.service.RegistrationMockService;

public class TalkGrid extends Grid<Talk> {

    public TalkGrid(RegistrationMockService registrationService) {
        super(Talk.class, false);

        addColumn(Talk::getTitle).setHeader("Title").setFlexGrow(2);
        addColumn(Talk::getSpeaker).setHeader("Speaker").setFlexGrow(1);
        addColumn(Talk::getLanguage).setHeader("Language").setFlexGrow(1);
        addColumn(Talk::getDescription).setHeader("Description").setFlexGrow(3);
        addColumn(talk -> talk.getCategory() != null ? talk.getCategory().getName() : "")
                .setHeader("Category").setFlexGrow(1);
        addComponentColumn(talk -> {
            Span registrations = new Span();
            SharedNumberSignal signal = registrationService.getRegistrationSignal(talk.getId());
            registrations.bindText(signal.map(c -> String.format("%.0f", c)));
            return registrations;
        }).setHeader("Registrations").setFlexGrow(1);
        setSelectionMode(SelectionMode.SINGLE);
        setSizeFull();
    }
}
