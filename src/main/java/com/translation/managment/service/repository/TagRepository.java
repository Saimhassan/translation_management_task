package com.translation.managment.service.repository;

import com.translation.managment.service.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByContextIgnoreCase(String context);
}
