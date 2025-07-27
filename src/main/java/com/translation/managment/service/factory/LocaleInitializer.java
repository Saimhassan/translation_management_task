package com.translation.managment.service.factory;

import com.translation.managment.service.entity.LocaleEntity;
import com.translation.managment.service.repository.LocaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocaleInitializer {

    private final LocaleRepository localeRepository;

    public LocaleEntity ensureDefaultLocale() {
        return localeRepository.findById(1L).orElseGet(() -> {
            LocaleEntity locale = new LocaleEntity();
            locale.setCode("en_US");
            locale.setName("English (United States)");
            return localeRepository.save(locale);
        });
    }

}
