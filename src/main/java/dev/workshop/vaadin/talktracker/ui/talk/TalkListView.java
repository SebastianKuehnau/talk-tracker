package dev.workshop.vaadin.talktracker.ui.talk;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.data.TalkRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route("")
public class TalkListView extends VerticalLayout {

    private final Notification aiFeedbackNotification = new Notification();

    final Grid<Talk> grid;
    @org.jetbrains.annotations.NotNull
    private final TalkRepository talkRepository;
    private final ChatClient chatClient;
    private final List<Talk> allTalks;
    private final TextField filterField;

    public TalkListView(TalkRepository talkRepository, ChatModel chatModel) {
        this.talkRepository = talkRepository;
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
                .system("You are a helpful assistant and help the user to find the right talk and show it in a grid. " +
                        "Keep the answer short.")
                .user(event.getValue())
                .tools(this)
                .stream()
                .content()
                .subscribe(token -> {},
                        throwable -> getUI().ifPresent(ui -> ui.access(() ->
                                Notification.show("Error - " + throwable.getLocalizedMessage())
                                        .addThemeVariants(NotificationVariant.ERROR))),
                    () -> getUI().ifPresent(ui -> ui.access(() -> {
                        filterField.clear();
                        filterField.setEnabled(true);
                    })));
    }

    @Tool(description = "Get a list of all scheduled conference talks with their id, title, category, speaker and language")
    List<Talk> getAllTalks() {
        return allTalks;
    }

    @Tool(description = """
                Search and filter conference talks shown in the grid. All parameters are optional — pass null to ignore.
                Returns the number of matching talks now shown in the grid.
            """)
    void filterTalks(
            @ToolParam(description = "Part of the talk title to match, or null") String title,
            @ToolParam(description = "Part of the speaker name to match, or null") String speaker,
            @ToolParam(description = "Track enum values to filter by, e.g. [GEN_AI, DATA_ML], or null") List<String> tracks,
            @ToolParam(description = "Date as yyyy-MM-dd, or null") String date,
            @ToolParam(description = "Earliest start time as HH:mm (inclusive), or null") String startTimeFrom,
            @ToolParam(description = "Latest start time as HH:mm (inclusive), or null") String startTimeTo,
            @ToolParam(description = "Room name or partial match, or null") String room,
            @ToolParam(description = "Talk format enum value, or null") String format
    ) {
        getUI().ifPresent(ui -> ui.access(() -> {
            var spec = buildSpecification(title, speaker, tracks, date, startTimeFrom, startTimeTo, room, format);
            grid.setItems(
                    query -> talkRepository.findAll(spec, VaadinSpringDataHelpers.toSpringPageRequest(query)).stream(),
                    query -> Math.toIntExact(talkRepository.count(spec)));
        }));
    }

    @Tool(description = "Current date and time")
    LocalDateTime currentLocalDateTime() {
        return LocalDateTime.now();
    }
    private Specification<Talk> buildSpecification(String title, String speaker, List<String> tracks,
                                          String date, String startTimeFrom, String startTimeTo,
                                          String room, String format) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (speaker != null && !speaker.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("speaker")), "%" + speaker.toLowerCase() + "%"));
            }
            if (tracks != null && !tracks.isEmpty()) {
                List<Talk.Track> trackEnums = tracks.stream()
                        .map(t -> Talk.Track.valueOf(t.toUpperCase()))
                        .toList();
                query.distinct(true);
                predicates.add(root.join("tracks").in(trackEnums));
            }
            if (date != null && !date.isBlank()) {
                predicates.add(cb.equal(root.get("startDate"), LocalDate.parse(date)));
            }
            if (startTimeFrom != null && !startTimeFrom.isBlank()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startTime"), LocalTime.parse(startTimeFrom)));
            }
            if (startTimeTo != null && !startTimeTo.isBlank()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startTime"), LocalTime.parse(startTimeTo)));
            }
            if (room != null && !room.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("room")), "%" + room.toLowerCase() + "%"));
            }
            if (format != null && !format.isBlank()) {
                predicates.add(cb.equal(root.get("format"), Talk.Format.valueOf(format.toUpperCase())));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
