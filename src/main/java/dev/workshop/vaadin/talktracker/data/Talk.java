package dev.workshop.vaadin.talktracker.data;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;

@Entity
public class Talk {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    private String title;

    @NonNull
    @Column(length = 5000)
    private String description;

    @NonNull
    private String speaker;

    @NonNull
    private String language;

    @ManyToOne
    private Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NonNull String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public @NonNull String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public @NonNull String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(@NonNull String speaker) {
        this.speaker = speaker;
    }

    public @NonNull String getLanguage() {
        return language;
    }

    public void setLanguage(@NonNull String language) {
        this.language = language;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

