package dev.workshop.vaadin.talktracker.ui.talks;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.data.TalkRepository;

@Route("")
public class TalkDetailsView extends HorizontalLayout {

    public TalkDetailsView(TalkRepository talkRepository) {

        var grid = new Grid<>(Talk.class);
        grid.setItems(talkRepository.findAll());
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
}
