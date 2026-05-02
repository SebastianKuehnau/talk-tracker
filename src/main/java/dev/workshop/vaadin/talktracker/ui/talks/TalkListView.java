package dev.workshop.vaadin.talktracker.ui.talks;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.data.TalkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Route("")
public class TalkListView extends VerticalLayout {

    private final Logger logger = LoggerFactory.getLogger(TalkListView.class);

    final Grid<Talk> grid;
    private final TalkRepository talkRepository;

    private final ChatClient chatClient;
    private final MessageList messageList;

    private final List<Talk> allTalks;

    public TalkListView(TalkRepository talkRepository, ChatClient.Builder chatClientBuilder) {
        this.talkRepository = talkRepository;

        chatClient = chatClientBuilder
                .build();

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

        messageList = new MessageList();
        messageList.setMarkdown(true);
        messageList.setSizeFull();

        var messageInput = new MessageInput(this::onSubmit);
        messageInput.setWidthFull();

        add(grid, messageList, messageInput);
        setSpacing(true);
        setPadding(true);
        setSizeFull();
    }

    private void onSubmit(MessageInput.SubmitEvent event) {
        var question = event.getValue();
        var userMessage = new MessageListItem(question, Instant.now(), "User");
        var assistantMessage = new MessageListItem("", "Assistant");

        messageList.addItem(userMessage);
        messageList.addItem(assistantMessage);

        chatClient.prompt()
                .system("""
                        You are a helpful assistant that helps users find conference talks at JAX 2026 (May 4-8, 2026).
                        You control a grid that displays talks. Use the filterTalks tool to filter the grid based on the 
                        user's request. Keep any return message as short as possible.
                        """)
                .user(question)
                .tools(this)
                .stream()
                .content()
                .subscribe(token -> {
                    getUI().ifPresent(ui -> ui.access(() -> assistantMessage.appendText(token)));
                });
    }

    @Tool(description = "Get a list of all scheduled conference talks with their id, title, category, speaker and language")
    List<Talk> getAllTalks() {
        return allTalks;
    }

    @Tool(description = "Filter the grid based on the filter")
    void filterTalks(@ToolParam(description = "ids of the filtered talks") List<String> ids) {
        logger.info("filterTalks: ids={}", ids);
        var filteredList = this.allTalks.stream()
                .filter(talk -> ids.contains(String.valueOf(talk.getId())))
                .toList();
        getUI().ifPresent(ui -> ui.access(() -> grid.setItems(filteredList)));
    }

    @Tool(description = "Current date and time")
    LocalDateTime currentLocalDateTime() {
        return LocalDateTime.now();
    }
}
