package com.translation.managment.service.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.translation.managment.service.dto.LocaleDTO;
import com.translation.managment.service.dto.TagDTO;
import com.translation.managment.service.dto.TranslationDTO;
import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.entity.TagEntity;
import com.translation.managment.service.entity.TranslationEntity;
import com.translation.managment.service.mapper.TagMapper;
import com.translation.managment.service.mapper.TranslationMapper;
import com.translation.managment.service.repository.LocaleRepository;
import com.translation.managment.service.repository.TagRepository;
import com.translation.managment.service.repository.TranslationRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TranslationServiceTest {

    @Mock
    TranslationRepository translationRepository;

    @Mock
    TranslationMapper translationMapper;

    @Mock
    TagRepository tagRepository;

    @Mock
    LocaleRepository localeRepository;

    @InjectMocks
    TranslationService translationService;

    private TranslationEntity mockEntity;
    private TranslationDTO mockDTO;
    private LocaleEntity mockLocale;
    private TagEntity mockTag;
    private TagDTO mockTagDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockLocale = new LocaleEntity(1L, "en", "English");
        mockTag = new TagEntity(1L, "common", new HashSet<>());
        mockTagDTO = new TagDTO(1L, "common");

        mockEntity = new TranslationEntity(1L, "key", "value", mockLocale, Set.of(mockTag), LocalDateTime.now());
        mockDTO = new TranslationDTO(1L, "key", "value", new LocaleDTO(1L, "en", "English"), List.of(mockTagDTO), LocalDateTime.now());
    }

    @Test
    void testCreateTranslation() {
        when(localeRepository.findByCode("en")).thenReturn(mockLocale);
        when(tagRepository.findByContextIgnoreCase("common")).thenReturn(Optional.of(mockTag));
        when(translationMapper.toEntity(any())).thenReturn(mockEntity);
        when(translationRepository.save(any())).thenReturn(mockEntity);
        when(translationMapper.toDto(any())).thenReturn(mockDTO);

        TranslationDTO result = translationService.create(mockDTO);

        assertNotNull(result);
        assertEquals("key", result.getKey());
        verify(translationRepository).save(any());
    }

    @Test
    void testCreateTranslationWithExistingLocaleAndTags() {
        LocaleDTO localeDTO = new LocaleDTO(1L, "en", "English");
        TagDTO tagDTO = new TagDTO();
        tagDTO.setContext("common");

        List<TagDTO> tagDTOs = new ArrayList<>();
        tagDTOs.add(tagDTO);

        TranslationDTO inputDto = new TranslationDTO();
        inputDto.setKey("key");
        inputDto.setContent("value");
        inputDto.setLocale(localeDTO);
        inputDto.setTags(tagDTOs);

        when(translationMapper.toEntity(inputDto)).thenReturn(mockEntity);
        when(localeRepository.findByCode("en")).thenReturn(mockLocale);
        when(tagRepository.findByContextIgnoreCase("common")).thenReturn(Optional.of(mockTag));
        when(translationRepository.save(mockEntity)).thenReturn(mockEntity);
        when(translationMapper.toDto(mockEntity)).thenReturn(inputDto);

        TranslationDTO result = translationService.create(inputDto);

        assertNotNull(result);
        assertEquals("key", result.getKey());
        verify(localeRepository).findByCode("en");
        verify(tagRepository).findByContextIgnoreCase("common");
        verify(translationRepository).save(mockEntity);
    }

    @Test
    void testCreateTranslationWithNewTagCreated() {
        LocaleDTO localeDTO = new LocaleDTO(1L, "en", "English");
        TagDTO tagDTO = new TagDTO();
        tagDTO.setContext("dynamic");

        List<TagDTO> tagDTOs = new ArrayList<>();
        tagDTOs.add(tagDTO);

        TranslationDTO inputDto = new TranslationDTO();
        inputDto.setKey("key2");
        inputDto.setContent("value2");
        inputDto.setLocale(localeDTO);
        inputDto.setTags(tagDTOs);

        TagEntity newTag = new TagEntity();
        newTag.setId(10L);
        newTag.setContext("dynamic");

        when(translationMapper.toEntity(inputDto)).thenReturn(mockEntity);
        when(localeRepository.findByCode("en")).thenReturn(mockLocale);
        when(tagRepository.findByContextIgnoreCase("dynamic")).thenReturn(Optional.empty());
        when(tagRepository.save(any())).thenReturn(newTag);
        when(translationRepository.save(mockEntity)).thenReturn(mockEntity);
        when(translationMapper.toDto(mockEntity)).thenReturn(inputDto);

        TranslationDTO result = translationService.create(inputDto);

        assertNotNull(result);
        verify(tagRepository).save(any());
    }

    @Test
    void testCreateTranslationWithNoTags() {
        LocaleDTO localeDTO = new LocaleDTO(1L, "en", "English");

        TranslationDTO inputDto = new TranslationDTO();
        inputDto.setKey("noTagsKey");
        inputDto.setContent("noTagsValue");
        inputDto.setLocale(localeDTO);
        inputDto.setTags(null);

        when(translationMapper.toEntity(inputDto)).thenReturn(mockEntity);
        when(localeRepository.findByCode("en")).thenReturn(mockLocale);
        when(translationRepository.save(mockEntity)).thenReturn(mockEntity);
        when(translationMapper.toDto(mockEntity)).thenReturn(inputDto);

        TranslationDTO result = translationService.create(inputDto);

        assertNotNull(result);
    }

    @Test
    void testCreateTranslationWithMissingLocaleShouldThrowException() {
        TranslationDTO dto = new TranslationDTO();
        LocaleDTO localeDTO = new LocaleDTO(null, "fr", "French");
        dto.setLocale(localeDTO);

        when(translationMapper.toEntity(dto)).thenReturn(mockEntity);
        when(localeRepository.findByCode("fr")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            translationService.create(dto);
        });

        assertEquals("Locale with code fr not found", exception.getMessage());
        verify(localeRepository).findByCode("fr");
    }

    @Test
    void testUpdateTranslation() {
        when(translationRepository.findById(1L)).thenReturn(Optional.of(mockEntity));
        when(localeRepository.findByCode("en")).thenReturn(mockLocale);
        when(tagRepository.findByContextIgnoreCase("common")).thenReturn(Optional.of(mockTag));
        when(translationRepository.save(any())).thenReturn(mockEntity);
        when(translationMapper.toDto(any())).thenReturn(mockDTO);

        TranslationDTO result = translationService.update(1L, mockDTO);

        assertNotNull(result);
        assertEquals("key", result.getKey());
        verify(translationRepository).save(any());
    }

    @Test
    void testGetById() {
        when(translationRepository.findById(1L)).thenReturn(Optional.of(mockEntity));
        when(translationMapper.toDto(any())).thenReturn(mockDTO);

        Optional<TranslationDTO> result = translationService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("key", result.get().getKey());
    }

    @Test
    void testGetAllTranslations() {
        Page<TranslationEntity> page = new PageImpl<>(List.of(mockEntity));
        when(translationRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(translationMapper.toDto(mockEntity)).thenReturn(mockDTO);

        Page<TranslationDTO> result = translationService.getAllTranslations(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testSearchTranslations() {
        when(translationRepository.searchByParams("key", "value", "common")).thenReturn(List.of(mockEntity));
        when(translationMapper.toDtoList(any())).thenReturn(List.of(mockDTO));

        List<TranslationDTO> result = translationService.searchTranslations("key", "value", "common");

        assertEquals(1, result.size());
        assertEquals("key", result.get(0).getKey());
    }

    @Test
    void testExportAllTranslations() throws Exception {
        when(translationRepository.streamAll()).thenReturn(Stream.of(mockEntity));
        when(translationMapper.toDto(mockEntity)).thenReturn(mockDTO);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        translationService.exportAllTranslations(out, objectMapper);

        String outputJson = out.toString();
        assertTrue(outputJson.contains("key"));
        assertTrue(outputJson.startsWith("["));
        assertTrue(outputJson.endsWith("]") || outputJson.endsWith("]\n"));
    }

}