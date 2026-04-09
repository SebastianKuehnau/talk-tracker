package dev.workshop.vaadin.talktracker.ui.talks;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.function.SerializableConsumer;

public class DescriptionForm extends VerticalLayout {

    public static final String SUMMARIZING = "summarizing...";
    final TextArea description;
    private final Button summarizeIcon;
    private final Paragraph summaryText;

    private SerializableConsumer<String> summarizeHandler;

    public DescriptionForm() {
        description = new TextArea("Description");
        description.setReadOnly(true);
        description.setWidthFull();
        description.setHeight("600px");

        summarizeIcon = new Button(VaadinIcon.MAGIC.create());
        summarizeIcon.addThemeVariants(ButtonVariant.TERTIARY);
        summarizeIcon.setEnabled(false);
        summarizeIcon.addClickListener(this::onSummarize);

        summaryText = new Paragraph();
        summaryText.getStyle().set("font-style", "italic");

        add(description, summarizeIcon, summaryText);
        setWidth("400px");
        setPadding(false);
    }

    private void onSummarize(ClickEvent<Button> event) {
        summaryText.setText(SUMMARIZING);
        summarizeIcon.setEnabled(false);
        this.summarizeHandler.accept(description.getValue());
    }

    public void setDescription(String description) {
        this.description.setValue(description);

        summarizeIcon.setEnabled(!description.isEmpty());
        this.summaryText.setText("");
    }

    public void setSummarizeHandler(SerializableConsumer<String> summarizeHandler) {
        this.summarizeHandler = summarizeHandler;
    }

    public void appendSummaryToken(String token) {
        if (summaryText.getText().equals(SUMMARIZING)) {
            summaryText.setText("");
        }

        summaryText.setText(summaryText.getText() + " " + token);
    }
}
