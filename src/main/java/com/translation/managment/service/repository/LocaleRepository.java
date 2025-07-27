package com.translation.managment.service.repository;

import com.translation.managment.service.entity.LocaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocaleRepository extends JpaRepository<LocaleEntity,Long> {
    LocaleEntity findByCode(String code);
}
