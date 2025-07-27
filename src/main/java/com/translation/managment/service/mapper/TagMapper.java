package com.translation.managment.service.mapper;

import com.translation.managment.service.dto.TagDTO;
import com.translation.managment.service.entity.TagEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDTO toDto(TagEntity entity);
    TagEntity toEntity(TagDTO dto);
    List<TagDTO> toDtoList(List<TagEntity> entities);
    List<TagEntity> toEntityList(List<TagDTO> dtos);
}
