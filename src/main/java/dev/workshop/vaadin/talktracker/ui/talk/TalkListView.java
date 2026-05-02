package dev.workshop.vaadin.talktracker.ui.talk;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.data.TalkRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("")
public class TalkListView extends VerticalLayout {

    private final Grid<Talk> grid;

    public TalkListView(TalkRepository talkRepository) {
        var filterField = new TextField("", "filter for ...");
        filterField.addValueChangeListener(this::onFilter);
        filterField.setValueChangeMode(ValueChangeMode.TIMEOUT);
        filterField.setValueChangeTimeout(500);
        filterField.setWidthFull();

        grid = new Grid<>(Talk.class);
        grid.setItems(query ->
                talkRepository.findAll(buildSpecification(filterField.getValue()), VaadinSpringDataHelpers.toSpringPageRequest(query)).stream(),
                query -> Math.toIntExact(talkRepository.count(buildSpecification(filterField.getValue()))));

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
        grid.getDataProvider().refreshAll();
    }

    private Specification<Talk> buildSpecification(String filterValue) {
        var lowerCaseValue = "%" + filterValue.toLowerCase() + "%";

        return (root, query, cb) ->
            cb.or(
                    cb.like(cb.lower(root.get("title")), lowerCaseValue),
                    cb.like(cb.lower(root.get("speaker")), lowerCaseValue),
                    cb.like(cb.lower(root.get("room")), lowerCaseValue),
                    cb.like(cb.lower(root.get("format")), lowerCaseValue)
            );
    }
}
