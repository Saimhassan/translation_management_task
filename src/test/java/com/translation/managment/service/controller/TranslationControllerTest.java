package com.translation.managment.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.translation.managment.service.dto.LocaleDTO;
import com.translation.managment.service.dto.TagDTO;
import com.translation.managment.service.dto.TranslationDTO;
import com.translation.managment.service.factory.TranslationDataFactory;
import com.translation.managment.service.service.TranslationService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class TranslationControllerTest {
    @Mock
    TranslationService service;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    TranslationDataFactory factory;
    @InjectMocks
    TranslationController translationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        TranslationDTO translationDTO =mock(TranslationDTO.class);
        when(service.create(any(TranslationDTO.class))).thenReturn(translationDTO);
        ResponseEntity<?> result = translationController.create(translationDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    void testCreate_WhenRuntimeException_ShouldReturnBadRequest() {
        TranslationDTO inputDto = new TranslationDTO();
        when(service.create(any(TranslationDTO.class)))
                .thenThrow(new RuntimeException("Validation failed"));

        ResponseEntity<?> result = translationController.create(inputDto);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertTrue(result.getBody().toString().contains("Validation failed"));
    }

    @Test
    void testUpdate() {
        TranslationDTO translationDTO = new TranslationDTO();
        translationDTO.setId(1L);
        translationDTO.setKey("key");
        translationDTO.setContent("content");

        LocaleDTO localeDTO = new LocaleDTO();
        localeDTO.setId(1L);
        localeDTO.setCode("code");
        localeDTO.setName("name");
        translationDTO.setLocale(localeDTO);

        List<TagDTO> tagDTOList = new ArrayList<>();
        TagDTO tagDTO = new TagDTO();
        tagDTO.setContext("context");
        tagDTO.setId(1L);
        tagDTOList.add(tagDTO);

        translationDTO.setTags(tagDTOList);
        translationDTO.setUpdatedAt(LocalDateTime.of(2025, Month.JULY, 26, 15, 49, 59));
        when(service.update(anyLong(), any(TranslationDTO.class))).thenReturn(translationDTO);

        ResponseEntity<TranslationDTO> result = translationController.update(1L, translationDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    void testGetById() {
        TranslationDTO translationDTO = new TranslationDTO(1L, "key", "content",
                new LocaleDTO(1L, "code", "name"), List.of(new TagDTO(1L, "context")),
                LocalDateTime.of(2025, Month.JULY, 26, 15, 49, 59));

        when(service.getById(anyLong())).thenReturn(Optional.of(translationDTO));

        ResponseEntity<TranslationDTO> result = translationController.getById(1L);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        Assertions.assertNotNull(result.getBody().getId());
        Assertions.assertNotNull(result.getBody().getKey());
        Assertions.assertNotNull(result.getBody().getContent());
        Assertions.assertNotNull(result.getBody().getTags());
        Assertions.assertNotNull(result.getBody().getUpdatedAt());
        Assertions.assertNotNull(result.getBody().getLocale());
    }

//    @Test
//    void testFindAll_WhenPageIsEmpty_ShouldReturnNoContent() {
//        Page<TranslationDTO> emptyPage = Page.empty();
//        when(service.getAllTranslations(any(Pageable.class))).thenReturn(emptyPage);
//
//        ResponseEntity<Page<TranslationDTO>> result = translationController.findAll(PageRequest.of(0, 20));
//
//        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
//        Assertions.assertNull(result.getBody());
//    }
//
//    @Test
//    void testFindAll_WhenPageHasContent_ShouldReturnOk() {
//        TranslationDTO dto = new TranslationDTO();
//        Page<TranslationDTO> pageWithData = new PageImpl<>(List.of(dto));
//        when(service.getAllTranslations(any(Pageable.class))).thenReturn(pageWithData);
//
//        ResponseEntity<Page<TranslationDTO>> result = translationController.findAll(PageRequest.of(0, 20));
//
//        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
//        Assertions.assertNotNull(result.getBody());
//        Assertions.assertEquals(1, result.getBody().getTotalElements());
//    }

    @Test
    void testSearchTranslations_WhenMatchesFound_ShouldReturnOk() {
        TranslationDTO translation = new TranslationDTO(
                1L,
                "key",
                "content",
                new LocaleDTO(1L, "code", "name"),
                List.of(new TagDTO(1L, "context")),
                LocalDateTime.of(2025, Month.JULY, 26, 15, 49, 59)
        );

        List<TranslationDTO> results = List.of(translation);
        when(service.searchTranslations("key", "content", "tag")).thenReturn(results);
        ResponseEntity<List<TranslationDTO>> response = translationController.searchTranslations("key", "content", "tag");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().size());

        TranslationDTO resultDto = response.getBody().get(0);
        Assertions.assertEquals("key", resultDto.getKey());
        Assertions.assertEquals("content", resultDto.getContent());
        Assertions.assertEquals("code", resultDto.getLocale().getCode());
        Assertions.assertEquals("context", resultDto.getTags().get(0).getContext());
    }


@Test
void testExportTranslations() throws IOException {
    HttpServletResponse response = mock(HttpServletResponse.class);
    ServletOutputStream outputStream = mock(ServletOutputStream.class);

    when(response.getOutputStream()).thenReturn(outputStream);

    translationController.exportTranslations(response);

    verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
    verify(response).setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=translations.json");
    verify(response).setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");

    verify(service).exportAllTranslations(outputStream, objectMapper);
}


@Test
void testGenerateTranslations_ShouldReturnOk() {
    ResponseEntity<?> result = translationController.generateTranslations();
    verify(factory).generateTranslations();
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertEquals("Translations inserted.", result.getBody());
}
}