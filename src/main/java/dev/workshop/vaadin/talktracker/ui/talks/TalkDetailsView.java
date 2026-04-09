package dev.workshop.vaadin.talktracker.ui.talks;

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
import org.springframework.data.jpa.domain.Specification;

@Route("")
public class TalkDetailsView extends VerticalLayout {

    private final Grid<Talk> grid;
    private final TalkRepository talkRepository;

    public TalkDetailsView(TalkRepository talkRepository) {
        this.talkRepository = talkRepository;

        var filterField = new TextField();
        filterField.setPlaceholder("Filter by title, speaker, language, category");
        filterField.setClearButtonVisible(true);
        filterField.setWidthFull();
        filterField.addValueChangeListener(this::onFilter);
        filterField.setValueChangeMode(ValueChangeMode.TIMEOUT);
        filterField.setValueChangeTimeout(500);
        add(filterField);

        grid = new Grid<>(Talk.class);
        grid.setItems(query -> talkRepository.findAll(
                        buildSpecification(filterField.getValue()),
                        VaadinSpringDataHelpers.toSpringPageRequest(query))
                .stream());
        grid.setColumns("title", "speaker", "language", "description", "category.name");

        grid.getColumnByKey("title").setFlexGrow(2);
        grid.getColumnByKey("description").setFlexGrow(3);
        grid.getColumnByKey("category.name").setHeader("Category");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.setSizeFull();

        add(grid);
        setSizeFull();
        setSpacing(true);
        setPadding(true);
    }

    private void onFilter(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        grid.getDataProvider().refreshAll();
    }

    private Specification<Talk> buildSpecification(String filterText) {

        var pattern = "%" + filterText.toLowerCase() + "%";

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("speaker")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("language")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern));
    }
}
