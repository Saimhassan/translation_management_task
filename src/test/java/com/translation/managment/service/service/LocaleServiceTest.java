package com.translation.managment.service.service;

import com.translation.managment.service.dto.LocaleDTO;
import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.mapper.LocaleMapper;
import com.translation.managment.service.repository.LocaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LocaleServiceTest {
    @Mock
    LocaleRepository localeRepository;
    @Mock
    LocaleMapper localeMapper;
    @InjectMocks
    LocaleService localeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLocale() {

        LocaleDTO inputDto = new LocaleDTO(1L, "en", "English");
        LocaleEntity mappedEntity = new LocaleEntity(1L, "en", "English");
        LocaleEntity savedEntity = new LocaleEntity(1L, "en", "English");
        LocaleDTO expectedDto = new LocaleDTO(1L, "en", "English");

        when(localeMapper.toEntity(inputDto)).thenReturn(mappedEntity);
        when(localeRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(localeMapper.toDto(savedEntity)).thenReturn(expectedDto);

        LocaleDTO result = localeService.createLocale(inputDto);

        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getCode(), result.getCode());
        assertEquals(expectedDto.getName(), result.getName());

        verify(localeMapper).toEntity(inputDto);
        verify(localeRepository).save(mappedEntity);
        verify(localeMapper).toDto(savedEntity);
    }
}
