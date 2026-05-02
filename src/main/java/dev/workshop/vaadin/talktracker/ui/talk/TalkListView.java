package dev.workshop.vaadin.talktracker.ui.talk;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.data.TalkRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("")
public class TalkListView extends VerticalLayout {

    private final List<Talk> allTalks;
    private final Grid<Talk> grid;

    public TalkListView(TalkRepository talkRepository) {
        var filterField = new TextField("", "filter for ...");
        filterField.addValueChangeListener(this::onFilter);
        filterField.setValueChangeMode(ValueChangeMode.TIMEOUT);
        filterField.setValueChangeTimeout(500);
        filterField.setWidthFull();

        grid = new Grid<>(Talk.class);
        allTalks = talkRepository.findAll();
        grid.setItems(allTalks);

        grid.setColumns("title", "speaker", "room");
        grid.getColumnByKey("room").setFlexGrow(0).setSortable(true).setHeader("Room");
        grid.addColumn(talk ->
                        talk.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM. EEE")))
                .setFlexGrow(0).setHeader("Date").setSortable(true);
        grid.addColumn(talk ->
                        talk.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                                talk.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .setFlexGrow(0).setHeader("Time").setSortable(true);
        grid.addColumn(talk -> talk.getTracks().stream()
                        .map(Talk.Track::getDisplayName)
                        .collect(Collectors.joining(", ")))
                .setSortable(true).setHeader("Tracks");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.setSizeFull();
        add(filterField, grid);
    }

    private void onFilter(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        var lowerCaseValue = event.getValue().toLowerCase();
        var filteredTalks = allTalks.stream()
                .filter(talk -> talk.getTitle().toLowerCase().contains(lowerCaseValue) ||
                        talk.getSpeaker().toLowerCase().contains(lowerCaseValue) ||
                        talk.getRoom().toLowerCase().contains(lowerCaseValue))
                .toList();
        grid.setItems(filteredTalks);
    }
}
