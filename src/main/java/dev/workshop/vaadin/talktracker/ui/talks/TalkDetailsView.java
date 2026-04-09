package dev.workshop.vaadin.talktracker.ui.talks;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import dev.workshop.vaadin.talktracker.ai.SummarizeAgent;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.data.TalkRepository;
import org.springframework.data.jpa.domain.Specification;

@Route("")
public class TalkDetailsView extends HorizontalLayout {

    final Grid<Talk> grid;
    final DescriptionForm descriptionForm;
    private final SummarizeAgent summarizeAgent;

    public TalkDetailsView(TalkRepository talkRepository, SummarizeAgent summarizeAgent) {
        this.summarizeAgent = summarizeAgent;

        var filterField = new TextField();
        filterField.setPlaceholder("filter for ...");
        filterField.setWidthFull();
        filterField.addValueChangeListener(this::onFilter);
        filterField.setClearButtonVisible(true);
        filterField.setValueChangeMode(ValueChangeMode.TIMEOUT);
        filterField.setValueChangeTimeout(1000);

        grid = new Grid<>(Talk.class);
        grid.setItems(query -> talkRepository.findAll(buildSpecification(filterField.getValue()),
                        VaadinSpringDataHelpers.toSpringPageRequest(query)).stream(),
                query -> Math.toIntExact(talkRepository.count(buildSpecification(filterField.getValue()))));
        grid.setSizeFull();

        grid.setColumns("title", "speaker", "category.name", "language", "description");
        grid.getColumnByKey("category.name").setHeader("Category");
        grid.getColumnByKey("title").setFlexGrow(2);
        grid.getColumnByKey("description").setFlexGrow(4);
        grid.asSingleSelect().addValueChangeListener(this::onSelect);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        var gridLayout = new VerticalLayout(filterField, grid);
        gridLayout.setPadding(false);
        descriptionForm = new DescriptionForm();

        add(gridLayout, descriptionForm);

        setPadding(true);
        setSizeFull();
    }

    private void onFilter(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        grid.getDataProvider().refreshAll();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        var ui = attachEvent.getUI();
        descriptionForm.setSummarizeHandler(description -> summarizeAgent.summarize(description)
                .subscribe(token -> ui.access(() -> descriptionForm.appendSummaryToken(token))));
    }

    private void onSelect(AbstractField.ComponentValueChangeEvent<Grid<Talk>, Talk> event) {
        descriptionForm.setDescription(event.getValue() == null ? "" : event.getValue().getDescription());
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
