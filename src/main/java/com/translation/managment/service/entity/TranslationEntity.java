package com.translation.managment.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranslationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "translation_key")
    private String key;

    @Column(name = "translation_content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locale_id", nullable = false)
    private LocaleEntity locale;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "translation_tags",
            joinColumns = @JoinColumn(name = "translation_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
