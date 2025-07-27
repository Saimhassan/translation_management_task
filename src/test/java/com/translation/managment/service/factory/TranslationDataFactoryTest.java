package com.translation.managment.service.factory;

import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.entity.TagEntity;
import com.translation.managment.service.entity.TranslationEntity;
import com.translation.managment.service.repository.TranslationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

class TranslationDataFactoryTest {

    @Mock
    TranslationRepository translationRepository;

    @Mock
    LocaleInitializer localeInitializer;

    @Mock
    TagInitializer tagInitializer;

    @Mock
    TranslationBatchBuilder batchBuilder;

    @InjectMocks
    TranslationDataFactory translationDataFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateTranslations() {
        LocaleEntity mockLocale = new LocaleEntity(1L, "en", "English");
        TagEntity mockTag = new TagEntity(1L, "context", Set.of());
        List<String> tagContexts = List.of("context");

        when(localeInitializer.ensureDefaultLocale()).thenReturn(mockLocale);
        when(tagInitializer.ensureTags()).thenReturn(Map.of("context", mockTag));
        when(tagInitializer.getTagContexts()).thenReturn(tagContexts);

        List<TranslationEntity> mockBatch = List.of(
                new TranslationEntity(1L,"key","content",null,null,null),
                new TranslationEntity()
        );

        when(batchBuilder.buildBatch(anyInt(), anyInt(), eq(mockLocale), anyMap(), eq(tagContexts)))
                .thenReturn(mockBatch);

        when(translationRepository.saveAll(mockBatch)).thenReturn(mockBatch);

        translationDataFactory.generateTranslations();

        verify(localeInitializer, times(1)).ensureDefaultLocale();
        verify(tagInitializer, times(1)).ensureTags();
        verify(tagInitializer, times(1)).getTagContexts();
        verify(batchBuilder, atLeastOnce()).buildBatch(anyInt(), anyInt(), eq(mockLocale), anyMap(), eq(tagContexts));
        verify(translationRepository, atLeastOnce()).saveAll(mockBatch);
    }
}