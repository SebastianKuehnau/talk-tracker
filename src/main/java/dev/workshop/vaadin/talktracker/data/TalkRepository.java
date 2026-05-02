package dev.workshop.vaadin.talktracker.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TalkRepository extends JpaRepository<Talk, Long>,
        JpaSpecificationExecutor<Talk> {
}
