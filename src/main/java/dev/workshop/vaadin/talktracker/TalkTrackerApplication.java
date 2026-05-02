package dev.workshop.vaadin.talktracker;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@PWA(name = "Talk Tracker", shortName = "Talk Tracker")
@Push
@StyleSheet(Aura.STYLESHEET)
@SpringBootApplication
public class TalkTrackerApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(TalkTrackerApplication.class, args);
	}

}
