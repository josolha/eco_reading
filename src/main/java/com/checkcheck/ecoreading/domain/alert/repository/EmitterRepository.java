package com.checkcheck.ecoreading.domain.alert.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EmitterRepository {

    // 모든 Emitters를 저장하는 ConcurrentHashMap
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(Long id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    public Optional<SseEmitter> get(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(emitters.get(userId));
    }

    public Collection<SseEmitter> findAll() {
        return emitters.values();
    }


    public void addEmitter(Long id, SseEmitter emitter) {
        if (id == null || emitter == null) {
            log.warn("Attempted to add null emitter or null id");
            return;
        }
        emitters.put(id, emitter);
    }

    public void removeEmitter(Long id) {
        if (id == null) {
            log.warn("Attempted to delete null emitter or null id");
            return;
        }
        emitters.remove(id);
    }

}