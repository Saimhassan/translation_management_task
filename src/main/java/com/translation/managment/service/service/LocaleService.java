package com.translation.managment.service.service;

import com.translation.managment.service.dto.LocaleDTO;
import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.mapper.LocaleMapper;
import com.translation.managment.service.repository.LocaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocaleService {

    @Autowired
    LocaleRepository localeRepository;

    @Autowired
    LocaleMapper localeMapper;

    public LocaleDTO createLocale(LocaleDTO localeDTO){
        LocaleEntity localeEntity = localeMapper.toEntity(localeDTO);
        LocaleEntity savedLocaleEntity = localeRepository.save(localeEntity);
        return localeMapper.toDto(savedLocaleEntity);
    }
}
