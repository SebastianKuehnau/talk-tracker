package dev.workshop.vaadin.talktracker.ui.talks;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import dev.workshop.vaadin.talktracker.data.CategoryRepository;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.data.TalkRepository;
import dev.workshop.vaadin.talktracker.service.SummaryAgent;
import dev.workshop.vaadin.talktracker.service.RegistrationMockService;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

@Route("")
public class TalkDetailsView extends HorizontalLayout {

    private final TalkRepository talkRepository;
    private final Optional<SummaryAgent> summaryAgent;

    final TalkGrid grid;
    final TalkForm form;

    final TextField filterField = new TextField("", "Filter by ...");

    public TalkDetailsView(TalkRepository talkRepository, CategoryRepository categoryRepository,
                           RegistrationMockService registrationService, Optional<SummaryAgent> summaryAgent) {
        this.talkRepository = talkRepository;
        this.summaryAgent = summaryAgent;

        grid = new TalkGrid(registrationService);
        form = new TalkForm(categoryRepository.findAll());

        Button newTalkButton = new Button("New Talk", this::onNewItem);
        newTalkButton.addThemeVariants(ButtonVariant.PRIMARY);

        grid.asSingleSelect().addValueChangeListener(this::onSelectItem);
        grid.setHeight("100%");
        grid.setItems(query -> talkRepository.findAll(
                buildSpecification(filterField.getValue()),
                VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());

        form.setOnSave(this::onSafeItem);
        form.setOnDelete(this::onDeleteItem);
        form.setWidth("400px");

        filterField.addValueChangeListener(this::onFilter);
        filterField.setValueChangeMode(ValueChangeMode.EAGER);
        filterField.setWidthFull();

        var gridLayout = new VerticalLayout(filterField, grid, newTalkButton);
        gridLayout.setHorizontalComponentAlignment(Alignment.END, newTalkButton);
        gridLayout.setPadding(false);

        add(gridLayout, form);
        setSizeFull();
        setFlexGrow(1, grid);
        setSpacing(true);
        setPadding(true);
    }

    private void onFilter(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        updateGrid();
    }

    private Specification<Talk> buildSpecification(String filterText) {
        if (filterText == null || filterText.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

        var pattern = "%" + filterText.toLowerCase() + "%";

        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            for (String field : List.of("title", "description", "speaker", "language")) {
                predicates.add(cb.like(cb.lower(root.get(field)), pattern));
            }

            var category = root.join("category", JoinType.LEFT);
            predicates.add(cb.like(cb.lower(category.get("name")), pattern));

            query.distinct(true);
            return cb.or(predicates.toArray(Predicate[]::new));
        };
    }

    private void onNewItem(ClickEvent<Button> event) {
        grid.asSingleSelect().clear();
        Talk newTalk = new Talk();
        form.setTalk(newTalk);
    }

    private void onSelectItem(AbstractField.ComponentValueChangeEvent<Grid<Talk>, Talk> event) {
        form.setTalk(event.getValue());
    }

    private void onSafeItem(Talk talk) {
        talkRepository.save(talk);
        updateGrid();
        form.setTalk(null);
        grid.asSingleSelect().clear();
    }

    private void onDeleteItem(Talk talk) {
        talkRepository.delete(talk);
        updateGrid();
        form.setTalk(null);
        grid.asSingleSelect().clear();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        var ui = attachEvent.getUI();

        summaryAgent.ifPresentOrElse(
                agent -> {
                    form.setOnSummarize(text -> {
                        CompletableFuture.supplyAsync(() -> agent.summarize(text))
                                .thenAccept(summary -> ui.access(() -> form.showSummary(summary)));
                    });
                },
                () -> Notification
                        .show("Summary service not available")
                        .addThemeVariants(NotificationVariant.LUMO_WARNING)
        );
    }

    private void updateGrid() {
        grid.getDataProvider().refreshAll();
    }
}
