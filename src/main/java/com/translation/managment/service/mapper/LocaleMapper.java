package com.translation.managment.service.mapper;

import com.translation.managment.service.dto.LocaleDTO;
import com.translation.managment.service.entity.LocaleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocaleMapper {
    LocaleDTO toDto(LocaleEntity entity);
    LocaleEntity toEntity(LocaleDTO dto);
}
