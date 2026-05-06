package dev.workshop.vaadin.talktracker.ui.talk;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.data.TalkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("")
public class TalkListView extends VerticalLayout {

    private final TextField filterField;
    Logger logger = LoggerFactory.getLogger(TalkListView.class);

    private final Grid<Talk> grid;
    private final ChatClient chatClient;
    private final List<Talk> allTalks;

    public TalkListView(TalkRepository talkRepository, ChatModel chatModel) {
        chatClient = ChatClient.builder(chatModel).build();

        filterField = new TextField("", "filter for ...");
        filterField.addValueChangeListener(this::onFilter);
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

        setSizeFull();
    }

    private void onFilter(AbstractField.ComponentValueChangeEvent<TextField, String> event) {

        if (event.getValue() == null || event.getValue().isBlank()) {
            return;
        }

        filterField.setEnabled(false);

        chatClient.prompt()
                .system("""
                        You are a helpful assistant that helps users find conference talks at JAX 2026 (May 4-8, 2026).
                                                You control a grid that displays talks. Use the searchTalks tool to filter the grid based on the user's request.
                                                Use showAllTalks to reset any active filter and show all talks again.
                                                Available tracks (use exact enum names): AGILE, AGILE_FLOW, ARCH, CLOUD, CORE_JAVA, DATA_ML, DEVOPS, GEN_AI, MICRO, PERF_SEC, SERVER_JAVA, WEB_JS.
                                                Available formats (use exact enum names): KEYNOTE, SESSION, WORKSHOP, PANEL, LAB, SHORTTALK.
                                                Don't show any return message.
                        """)
                .user(event.getValue())
                .tools(this)
                .stream()
                .content()
                .subscribe(token -> {},
                        throwable -> getUI().ifPresent(ui -> ui.access(() ->
                                Notification.show("Error - " + throwable.getLocalizedMessage())
                                        .addThemeVariants(NotificationVariant.ERROR))),
                    () -> getUI().ifPresent(ui -> ui.access(() -> {
                        filterField.setEnabled(true);
                        filterField.clear();
                    })));
    }

    @Tool(description = "Get a list of all scheduled conference talks with their id, title, tracks, startDate, startTime, speaker and language")
    List<Talk> getAllTalks() {
        logger.info("Getting all talks");
        return allTalks;
    }

    @Tool(description = """
                Search and filter conference talks shown in the grid. All parameters are optional — pass null to ignore.
                            Tracks (exact enum names): AGILE, AGILE_FLOW, ARCH, CLOUD, CORE_JAVA, DATA_ML, DEVOPS, GEN_AI, MICRO, PERF_SEC, SERVER_JAVA, WEB_JS.
                            Formats (exact enum names): KEYNOTE, SESSION, WORKSHOP, PANEL, LAB, SHORTTALK.
                            Date format: yyyy-MM-dd-EEE (conference runs 2026-05-04 Monday to 2026-05-08 Friday).
                            Time format: HH:mm.
                            Returns the number of matching talks now shown in the grid.
            """)
    void filterTalks(@ToolParam(description = "ids of the filtered talks") List<Long> ids) {
        logger.info("Filtering talks with ids {}", ids);

        var filteredList = this.allTalks.stream()
                .filter(talk -> ids.contains(talk.getId()))
                .toList();
        getUI().ifPresent(ui -> ui.access(() -> grid.setItems(filteredList)));
    }

    @Tool(description = "Current date and time")
    LocalDateTime currentLocalDateTime() {
        return LocalDateTime.now();
    }
}
