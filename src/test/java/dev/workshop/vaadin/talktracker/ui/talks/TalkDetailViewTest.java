package dev.workshop.vaadin.talktracker.ui.talks;

import com.vaadin.browserless.SpringBrowserlessTest;
import dev.workshop.vaadin.talktracker.data.TalkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TalkDetailViewTest extends SpringBrowserlessTest {

    private TalkDetailsView view;

    @Autowired
    TalkRepository talkRepository;

    @BeforeEach
    public void setUp() {
        view = navigate(TalkDetailsView.class);
    }

    @Test
    public void allDataVisibleInGrid() {
        assertEquals(view.grid.getGenericDataView().getItems().count(), talkRepository.count());
    }
}
