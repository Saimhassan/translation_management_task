package com.translation.managment.service.factory;

import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.entity.TagEntity;
import com.translation.managment.service.entity.TranslationEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class TranslationBatchBuilder {
    public List<TranslationEntity> buildBatch(int start, int end, LocaleEntity locale, Map<String, TagEntity> contextToTag, List<String> tagContexts) {
        List<TranslationEntity> batch = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            TranslationEntity translation = new TranslationEntity();
            translation.setKey("key_" + i);
            translation.setContent("Sample translation content " + i);
            translation.setLocale(locale);
            translation.setUpdatedAt(LocalDateTime.now());

            List<String> shuffled = new ArrayList<>(tagContexts);
            Collections.shuffle(shuffled);

            Set<TagEntity> tags = new HashSet<>();
            tags.add(contextToTag.get(shuffled.get(0)));
            tags.add(contextToTag.get(shuffled.get(1)));
            translation.setTags(tags);

            batch.add(translation);
        }
        return batch;
    }
}
