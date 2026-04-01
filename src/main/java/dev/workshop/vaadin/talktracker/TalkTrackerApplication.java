package dev.workshop.vaadin.talktracker;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.aura.Aura;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Push
@EnableScheduling
@StyleSheet(Aura.STYLESHEET)
@SpringBootApplication
public class TalkTrackerApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(TalkTrackerApplication.class, args);
	}

}
