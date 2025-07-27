package com.translation.managment.service.factory;

import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.entity.TagEntity;
import com.translation.managment.service.entity.TranslationEntity;
import com.translation.managment.service.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationDataFactory {
    private final TranslationRepository translationRepository;
    private final LocaleInitializer localeInitializer;
    private final TagInitializer tagInitializer;
    private final TranslationBatchBuilder batchBuilder;

    private static final int TOTAL_RECORDS = 100_000;
    private static final int BATCH_SIZE = 1000;

    public void generateTranslations() {
        LocaleEntity locale = localeInitializer.ensureDefaultLocale();
        Map<String, TagEntity> contextToTag = tagInitializer.ensureTags();
        List<String> tagContexts = tagInitializer.getTagContexts();

        for (int i = 1; i <= TOTAL_RECORDS; i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE - 1, TOTAL_RECORDS);
            List<TranslationEntity> batch = batchBuilder.buildBatch(i, end, locale, contextToTag, tagContexts);
            translationRepository.saveAll(batch);
            System.out.println("Inserted up to " + end);
        }

        System.out.println("🎉 Finished inserting " + TOTAL_RECORDS + " translation records.");
    }
}
