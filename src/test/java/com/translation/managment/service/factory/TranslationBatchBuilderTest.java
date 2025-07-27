package com.translation.managment.service.factory;

import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.entity.TagEntity;
import com.translation.managment.service.entity.TranslationEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class TranslationBatchBuilderTest {
    TranslationBatchBuilder translationBatchBuilder = new TranslationBatchBuilder();

    @Test
    void testBuildBatch_ShouldCreateTranslationEntities() {
        int start = 1;
        int end = 2;

        LocaleEntity locale = new LocaleEntity(1L, "en_US", "English (US)");
        TagEntity tag1 = new TagEntity(1L, "Context1", new HashSet<>());
        TagEntity tag2 = new TagEntity(2L, "Context2", new HashSet<>());

        Map<String, TagEntity> contextToTag = Map.of(
                "Context1", tag1,
                "Context2", tag2
        );

        List<String> tagContexts = List.of("Context1", "Context2");

        List<TranslationEntity> result = translationBatchBuilder.buildBatch(start, end, locale, contextToTag, tagContexts);

        Assertions.assertEquals(2, result.size());

        for (int i = 0; i < result.size(); i++) {
            TranslationEntity t = result.get(i);
            Assertions.assertEquals("key_" + (start + i), t.getKey());
            Assertions.assertEquals("Sample translation content " + (start + i), t.getContent());
            Assertions.assertEquals(locale, t.getLocale());
            Assertions.assertEquals(2, t.getTags().size());
            Assertions.assertNotNull(t.getUpdatedAt());
        }
    }
}
