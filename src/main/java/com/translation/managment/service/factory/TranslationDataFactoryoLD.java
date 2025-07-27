//package com.translation.managment.service.factory;
//
//import com.translation.managment.service.entity.LocaleEntity;
//import com.translation.managment.service.entity.TagEntity;
//import com.translation.managment.service.entity.TranslationEntity;
//import com.translation.managment.service.repository.LocaleRepository;
//import com.translation.managment.service.repository.TagRepository;
//import com.translation.managment.service.repository.TranslationRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//@Component
//@RequiredArgsConstructor
//public class TranslationDataFactory {
//
//    private final TranslationRepository translationRepository;
//    private final LocaleRepository localeRepository;
//    private final TagRepository tagRepository;
//
//    private static final int TOTAL_RECORDS = 100_000;
//    private static final int BATCH_SIZE = 1000;
//
//    private static final List<String> TAG_CONTEXTS = List.of("mobile", "desktop", "web");
//
//    public void generateTranslations() {
//
//        // Ensure locale exists
//        LocaleEntity locale = localeRepository.findById(1L).orElseGet(() -> {
//            LocaleEntity newLocale = new LocaleEntity();
//            newLocale.setCode("en_US");
//            newLocale.setName("English (United States)");
//            return localeRepository.save(newLocale);
//        });
//
//        // Ensure tags exist
//        Map<String, TagEntity> contextToTag = new HashMap<>();
//        for (String context : TAG_CONTEXTS) {
//            TagEntity tag = tagRepository.findByContextIgnoreCase(context)
//                    .orElseGet(() -> tagRepository.save(new TagEntity(null, context, new HashSet<>())));
//            contextToTag.put(context, tag);
//        }
//
//        List<TranslationEntity> batch = new ArrayList<>();
//
//        for (int i = 1; i <= TOTAL_RECORDS; i++) {
//            TranslationEntity translation = new TranslationEntity();
//            translation.setKey("key_" + i);
//            translation.setContent("Sample translation content " + i);
//            translation.setLocale(locale);
//            translation.setUpdatedAt(LocalDateTime.now());
//
//            // Randomly assign 2 tags
//            List<String> shuffled = new ArrayList<>(TAG_CONTEXTS);
//            Collections.shuffle(shuffled);
//            Set<TagEntity> tags = new HashSet<>();
//            tags.add(contextToTag.get(shuffled.get(0)));
//            tags.add(contextToTag.get(shuffled.get(1)));
//            translation.setTags(tags);
//
//            batch.add(translation);
//
//            if (i % BATCH_SIZE == 0) {
//                translationRepository.saveAll(batch);
//                batch.clear();
//                System.out.println("✅ Inserted up to " + i);
//            }
//        }
//
//        if (!batch.isEmpty()) {
//            translationRepository.saveAll(batch);
//        }
//
//        System.out.println("🎉 Finished inserting " + TOTAL_RECORDS + " translation records with tags.");
//    }
//}
