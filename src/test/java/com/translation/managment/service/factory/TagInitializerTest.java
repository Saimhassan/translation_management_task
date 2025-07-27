package com.translation.managment.service.factory;

import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.entity.TagEntity;
import com.translation.managment.service.entity.TranslationEntity;
import com.translation.managment.service.repository.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.mockito.Mockito.*;

class TagInitializerTest {
    @Mock
    TagRepository tagRepository;
    @Mock
    List<String> DEFAULT_CONTEXTS;
    @InjectMocks
    TagInitializer tagInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnsureTags_WhenTagsDoNotExist_ShouldCreateAndReturn() {
        TagEntity tagA = new TagEntity(1L, "contextA", new HashSet<>());
        TagEntity tagB = new TagEntity(2L, "contextB", new HashSet<>());

        when(tagRepository.findByContextIgnoreCase("contextA")).thenReturn(Optional.empty());
        when(tagRepository.findByContextIgnoreCase("contextB")).thenReturn(Optional.empty());

        when(tagRepository.save(any(TagEntity.class)))
                .thenAnswer(invocation -> {
                    TagEntity tag = invocation.getArgument(0);
                    if ("contextA".equalsIgnoreCase(tag.getContext())) {
                        return tagA;
                    } else if ("contextB".equalsIgnoreCase(tag.getContext())) {
                        return tagB;
                    }
                    return tag;
                });

        Map<String, TagEntity> result = tagInitializer.ensureTags();

        Assertions.assertNotNull(result);
    }

    @Test
    void testGetTagContexts_ShouldReturnDefaultContexts() {
        List<String> result = tagInitializer.getTagContexts();
        Assertions.assertNotNull(result);
    }
}