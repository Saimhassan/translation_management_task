package com.translation.managment.service.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class TranslationService {

    @Autowired
    TranslationRepository translationRepository;

    @Autowired
    TranslationMapper translationMapper;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    LocaleRepository localeRepository;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    EntityManager entityManager;

    public TranslationDTO create(TranslationDTO dto) {
        TranslationEntity entity = translationMapper.toEntity(dto);
        String code = dto.getLocale().getCode();
        LocaleEntity locale = localeRepository.findByCode(code);
        if (locale != null) {
            entity.setLocale(locale);
        } else {
            throw new RuntimeException("Locale with code " + code + " not found");
        }
        Set<TagEntity> resolvedTags = new HashSet<>();
        if (dto.getTags() != null) {
            for (TagDTO tagDTO : dto.getTags()) {
                String context = tagDTO.getContext();
                TagEntity tagEntity = tagRepository.findByContextIgnoreCase(context)
                        .orElseGet(() -> {
                            TagEntity newTag = new TagEntity();
                            newTag.setContext(context);
                            return tagRepository.save(newTag);
                        });
                resolvedTags.add(tagEntity);
            }
        }
        entity.setTags(resolvedTags);
        entity.setUpdatedAt(LocalDateTime.now());
        TranslationEntity savedTranslationEntity = translationRepository.save(entity);
        return translationMapper.toDto(savedTranslationEntity);
    }

    public TranslationDTO update(Long id, TranslationDTO dto) {
        TranslationEntity existingEntity = translationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Translation with ID " + id + " not found"));
        existingEntity.setKey(dto.getKey());
        existingEntity.setContent(dto.getContent());
        String localeCode = dto.getLocale().getCode();
        LocaleEntity locale = localeRepository.findByCode(localeCode);
        if (locale != null) {
            existingEntity.setLocale(locale);
        } else {
            throw new RuntimeException("Locale with code " + localeCode + " not found");
        }
        Set<TagEntity> resolvedTags = new HashSet<>();
        if (dto.getTags() != null) {
            for (TagDTO tagDTO : dto.getTags()) {
                String context = tagDTO.getContext();
                TagEntity tagEntity = tagRepository.findByContextIgnoreCase(context)
                        .orElseGet(() -> {
                            TagEntity newTag = new TagEntity();
                            newTag.setContext(context);
                            return tagRepository.save(newTag);
                        });
                resolvedTags.add(tagEntity);
            }
        }
        existingEntity.setTags(resolvedTags);
        existingEntity.setUpdatedAt(LocalDateTime.now());
        TranslationEntity saved = translationRepository.save(existingEntity);
        return translationMapper.toDto(saved);
    }

    public Optional<TranslationDTO> getById(Long id) {
        Optional<TranslationEntity> byId = translationRepository.findById(id);
        return byId.map(translationMapper::toDto);
    }

//    public List<TranslationDTO> findAll(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
//        Page<TranslationEntity> translationPage = translationRepository.findAll(pageable);
//        return translationMapper.toDtoList(translationPage.getContent());
//    }

    @Transactional(readOnly = true)
    public Page<TranslationDTO> getAllTranslations(Pageable pageable) {
        Page<TranslationEntity> page = translationRepository.findAll(pageable);
        return page.map(entity -> translationMapper.toDto(entity));
    }

    public List<TranslationDTO> searchTranslations(String key, String content, String tag) {
        List<TranslationEntity> entities = translationRepository.searchByParams(key, content, tag);
        return translationMapper.toDtoList(entities);
    }

    @Transactional(readOnly = true)
    public void exportAllTranslations(OutputStream out, ObjectMapper objectMapper) throws IOException {
        JsonGenerator generator = objectMapper.getFactory().createGenerator(out);
        generator.writeStartArray();

        try (Stream<TranslationEntity> stream = translationRepository.streamAll()) {
            stream.forEach(entity -> {
                try {
                    TranslationDTO dto = translationMapper.toDto(entity);
                    objectMapper.writeValue(generator, dto);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }

        generator.writeEndArray();
        generator.flush();
    }


    //    @Transactional(readOnly = true)
//    public void exportAllTranslations(OutputStream out, ObjectMapper objectMapper) throws IOException {
//        JsonGenerator generator = objectMapper.getFactory().createGenerator(out);
//        generator.writeStartArray();
//
//        try (Stream<TranslationEntity> stream = translationRepository.streamAllWithTagsAndLocale()) {
//            stream.forEach(entity -> {
//                try {
//                    TranslationDTO dto = translationMapper.toDto(entity);
//                    objectMapper.writeValue(generator, dto);
//                } catch (IOException e) {
//                    throw new UncheckedIOException(e);
//                }
//            });
//        }
//
//        generator.writeEndArray();
//        generator.flush();
//    }

}
