package dev.workshop.vaadin.talktracker.ui.talks;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.workshop.vaadin.talktracker.data.Category;
import dev.workshop.vaadin.talktracker.data.Talk;
import java.util.List;


public class TalkForm extends FormLayout {

    final TextField title = new TextField("Title");
    final TextField speaker = new TextField("Speaker");
    final TextField language = new TextField("Language");
    final TextArea description = new TextArea("Description");

    final Button summarize = new Button(VaadinIcon.MAGIC.create());
    final Paragraph summaryResult = new Paragraph();
    final ComboBox<Category> category = new ComboBox<>("Category");

    final Button save = new Button("Save");
    final Button delete = new Button(VaadinIcon.TRASH.create());
    final Button cancel = new Button("Cancel");

    private final Binder<Talk> binder = new BeanValidationBinder<>(Talk.class);
    private Talk currentTalk;

    private SerializableConsumer<Talk> onSave;
    private SerializableConsumer<Talk> onDelete;
    private SerializableConsumer<String> onSummarize;

    public TalkForm(List<Category> categories) {
        description.setHeight("100px");
        description.setWidthFull();

        category.setItems(categories);
        category.setItemLabelGenerator(Category::getName);

        binder.forField(title).asRequired("Title is required").bind(Talk::getTitle, Talk::setTitle);
        binder.forField(speaker).asRequired("Speaker is required").bind(Talk::getSpeaker, Talk::setSpeaker);
        binder.forField(language).asRequired("Language is required").bind(Talk::getLanguage, Talk::setLanguage);
        binder.forField(description).asRequired("Description is required").bind(Talk::getDescription, Talk::setDescription);
        binder.forField(category).bind(Talk::getCategory, Talk::setCategory);

        save.addThemeVariants(ButtonVariant.PRIMARY);
        delete.addThemeVariants(ButtonVariant.ERROR, ButtonVariant.TERTIARY);
        summarize.addThemeVariants(ButtonVariant.TERTIARY, ButtonVariant.SMALL);

        save.addClickListener(this::onSave);
        delete.addClickListener(this::onDelete);
        cancel.addClickListener(this::onCancel);
        summarize.addClickListener(this::onSummarize);

        summaryResult.setVisible(false);
        summaryResult.getStyle().set("font-style", "italic");

        var buttons = new HorizontalLayout(save, delete, cancel);

        var descriptionLayout = new VerticalLayout(description, summarize);
        descriptionLayout.setAlignItems(FlexComponent.Alignment.END);
        descriptionLayout.setSpacing(false);
        descriptionLayout.setPadding(false);
        descriptionLayout.setWidthFull();

        add(title, speaker, language, category, descriptionLayout, summaryResult, buttons);

        setTalk(null);
    }

    private void onSummarize(ClickEvent<Button> event) {
        var text = description.getValue();
        if (onSummarize != null && text != null && !text.isBlank()) {
            summaryResult.setText("Summarizing...");
            summaryResult.setVisible(true);
            onSummarize.accept(text);
            summarize.setVisible(false);
        }
    }

    private void onCancel(ClickEvent<Button> event) {
        binder.readBean(currentTalk);
        if (currentTalk == null ||currentTalk.getId() == null) {
            setTalk(null);
        }
    }

    private void onDelete(ClickEvent<Button> event) {
        if (onDelete != null && currentTalk != null) {
            onDelete.accept(currentTalk);
        }
    }

    private void onSave(ClickEvent<Button> buttonClickEvent) {
        if (currentTalk == null) {
            return;
        }
        if (binder.writeBeanIfValid(currentTalk) && onSave != null) {
            onSave.accept(currentTalk);
        }
    }

    public void setTalk(Talk talk) {
        this.currentTalk = talk;
        binder.readBean(talk);

        var hasTalk = talk != null;
        var isExisting = hasTalk && talk.getId() != null;

        title.setEnabled(hasTalk);
        speaker.setEnabled(hasTalk);
        language.setEnabled(hasTalk);
        description.setEnabled(hasTalk);
        category.setEnabled(hasTalk);

        save.setVisible(hasTalk);
        cancel.setVisible(hasTalk);
        delete.setVisible(isExisting);
        summarize.setVisible(hasTalk);
        summaryResult.setVisible(false);
    }

    public void setOnSave(SerializableConsumer<Talk> onSave) {
        this.onSave = onSave;
    }

    public void setOnDelete(SerializableConsumer<Talk> onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnSummarize(SerializableConsumer<String> onSummarize) {
        this.onSummarize = onSummarize;
    }

    public void showSummary(String summary) {
        summaryResult.setText(summary);
        summaryResult.setVisible(true);
        summarize.setVisible(false);
    }
}
