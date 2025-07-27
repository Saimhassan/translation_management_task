package com.translation.managment.service.factory;

import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.repository.LocaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class LocaleInitializerTest {
    @Mock
    LocaleRepository localeRepository;
    @InjectMocks
    LocaleInitializer localeInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnsureDefaultLocale_WhenLocaleNotFound_ShouldCreateAndReturnDefault() {
        // Arrange
        LocaleEntity savedLocale = new LocaleEntity();
        savedLocale.setId(1L);
        savedLocale.setCode("en_US");
        savedLocale.setName("English (United States)");

        when(localeRepository.findById(1L)).thenReturn(Optional.empty());
        when(localeRepository.save(any(LocaleEntity.class))).thenReturn(savedLocale);

        // Act
        LocaleEntity result = localeInitializer.ensureDefaultLocale();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("en_US", result.getCode());
        Assertions.assertEquals("English (United States)", result.getName());
    }

    @Test
    void testEnsureDefaultLocale_WhenLocaleExists_ShouldReturnExisting() {
        // Arrange
        LocaleEntity existingLocale = new LocaleEntity();
        existingLocale.setId(1L);
        existingLocale.setCode("en_GB");
        existingLocale.setName("English (UK)");

        when(localeRepository.findById(1L)).thenReturn(Optional.of(existingLocale));

        // Act
        LocaleEntity result = localeInitializer.ensureDefaultLocale();

        // Assert
        Assertions.assertEquals(existingLocale, result);
    }

}