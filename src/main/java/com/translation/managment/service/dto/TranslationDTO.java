package com.translation.managment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationDTO {
    private Long id;
    private String key;
    private String content;
    private LocaleDTO locale;
    private List<TagDTO> tags;
    private LocalDateTime updatedAt;
}
