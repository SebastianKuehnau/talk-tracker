package dev.workshop.vaadin.talktracker.ui.talks;

import com.vaadin.browserless.SpringBrowserlessTest;
import dev.workshop.vaadin.talktracker.data.Talk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TalkDetailsViewTest extends SpringBrowserlessTest {

    private TalkDetailsView view;

    @BeforeEach
    void setUp() {
        view = navigate(TalkDetailsView.class);
    }

    @Test
    void gridShowsTalks() {
        assertTrue(test(view.grid).size() == 20, "Grid should contain talks from DataInitializer");
    }

    @Test
    void selectingTalkInGridPopulatesForm() {
        Talk firstTalk = view.grid.getGenericDataView().getItems().findFirst().orElseThrow();
        view.grid.select(firstTalk);

        assertEquals(firstTalk.getTitle(), view.form.title.getValue());
        assertEquals(firstTalk.getSpeaker(), view.form.speaker.getValue());
        assertEquals(firstTalk.getLanguage(), view.form.language.getValue());
        assertEquals(firstTalk.getDescription(), view.form.description.getValue());
        assertTrue(view.form.title.isEnabled(), "Title field should be enabled after selection");
    }

    @Test
    void deselectingTalkClearsAndDisablesForm() {
        Talk firstTalk = view.grid.getGenericDataView().getItems().findFirst().orElseThrow();
        view.grid.select(firstTalk);
        view.grid.deselectAll();

        assertFalse(view.form.title.isEnabled(), "Title field should be disabled when no talk selected");
        assertFalse(view.form.speaker.isEnabled(), "Speaker field should be disabled when no talk selected");
        assertEquals("", view.form.title.getValue());
    }

    @Test
    void selectingDifferentTalkUpdatesForm() {
        var talks = view.grid.getGenericDataView().getItems().toList();
        assertTrue(talks.size() >= 2, "Need at least 2 talks");

        view.grid.select(talks.get(0));
        assertEquals(talks.get(0).getTitle(), view.form.title.getValue());

        view.grid.select(talks.get(1));
        assertEquals(talks.get(1).getTitle(), view.form.title.getValue());
        assertEquals(talks.get(1).getSpeaker(), view.form.speaker.getValue());
    }
}
