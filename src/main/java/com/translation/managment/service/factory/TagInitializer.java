package com.translation.managment.service.factory;

import com.translation.managment.service.entity.TagEntity;
import com.translation.managment.service.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TagInitializer {
    private final TagRepository tagRepository;

    private static final List<String> DEFAULT_CONTEXTS = List.of("mobile", "desktop", "web");

    public Map<String, TagEntity> ensureTags() {
        Map<String, TagEntity> contextToTag = new HashMap<>();
        for (String context : DEFAULT_CONTEXTS) {
            TagEntity tag = tagRepository.findByContextIgnoreCase(context)
                    .orElseGet(() -> tagRepository.save(new TagEntity(null, context, new HashSet<>())));
            contextToTag.put(context, tag);
        }
        return contextToTag;
    }

    public List<String> getTagContexts() {
        return DEFAULT_CONTEXTS;
    }
}
