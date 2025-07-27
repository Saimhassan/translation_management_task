package com.translation.managment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocaleDTO {
    private Long id;
    private String code;
    private String name;
}
