package dev.workshop.vaadin.talktracker.service;

import com.vaadin.flow.signals.shared.SharedNumberSignal;
import dev.workshop.vaadin.talktracker.data.Talk;
import dev.workshop.vaadin.talktracker.data.TalkRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RegistrationMockService {

    private final TalkRepository talkRepository;
    private final Random random = new Random();
    private final Map<Long, SharedNumberSignal> signals = new ConcurrentHashMap<>();

    public RegistrationMockService(TalkRepository talkRepository) {
        this.talkRepository = talkRepository;
    }

    public SharedNumberSignal getRegistrationSignal(Long talkId) {
        return signals.computeIfAbsent(talkId, id -> new SharedNumberSignal());
    }

    @Scheduled(fixedRate = 300)
    public void simulateRegistrations() {
        List<Talk> talks = talkRepository.findAll();
        if (talks.isEmpty()) {
            return;
        }
        Talk talk = talks.get(random.nextInt(talks.size()));
        SharedNumberSignal signal = getRegistrationSignal(talk.getId());
        signal.incrementBy(random.nextInt(1, 6));
    }
}
