package com.translation.managment.service.repository;

import com.translation.managment.service.entity.TranslationEntity;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

public interface TranslationRepository extends JpaRepository<TranslationEntity,Long> {

    @Query("""
    SELECT DISTINCT t FROM TranslationEntity t
    LEFT JOIN t.tags tag
    WHERE (:key IS NULL OR LOWER(t.key) LIKE LOWER(CONCAT('%', :key, '%')))
      AND (:content IS NULL OR LOWER(t.content) LIKE LOWER(CONCAT('%', :content, '%')))
      AND (:tag IS NULL OR LOWER(tag.context) LIKE LOWER(CONCAT('%', :tag, '%')))
""")
    List<TranslationEntity> searchByParams(@Param("key") String key,
                                           @Param("content") String content,
                                           @Param("tag") String tag);

    @Query("SELECT t FROM TranslationEntity t WHERE t.locale.code = :localeCode")
    @QueryHints({
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE),
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true"),
            @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "false")
    })
    @EntityGraph(attributePaths = {"locale", "tags"})
    Stream<TranslationEntity> streamByLocaleCode(@Param("localeCode") String localeCode);

//    @EntityGraph(attributePaths = {"locale", "tags"})
//    Page<TranslationEntity> findAll(Pageable pageable);
}
