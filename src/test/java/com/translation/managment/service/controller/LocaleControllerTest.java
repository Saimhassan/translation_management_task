package com.translation.managment.service.controller;

import com.translation.managment.service.dto.LocaleDTO;
import com.translation.managment.service.service.LocaleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

class LocaleControllerTest {
    @Mock
    LocaleService localeService;
    @InjectMocks
    LocaleController localeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {

        LocaleDTO localeDTO = new LocaleDTO(1L, "code","name");

        when(localeService.createLocale(any(LocaleDTO.class))).thenReturn(localeDTO);

        ResponseEntity<LocaleDTO> result = localeController.create(localeDTO);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        Assertions.assertNotNull(result.getBody().getCode());
        Assertions.assertNotNull(result.getBody().getId());
        Assertions.assertNotNull(result.getBody().getName());
    }
}