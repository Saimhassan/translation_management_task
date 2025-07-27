package com.translation.managment.service.mapper;

import com.translation.managment.service.dto.TranslationDTO;
import com.translation.managment.service.entity.TranslationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { LocaleMapper.class, TagMapper.class })
public interface TranslationMapper {
    TranslationDTO toDto(TranslationEntity entity);

    TranslationEntity toEntity(TranslationDTO dto);

    List<TranslationDTO> toDtoList(List<TranslationEntity> entities);
    List<TranslationEntity> toEntityList(List<TranslationDTO> dtos);
}
