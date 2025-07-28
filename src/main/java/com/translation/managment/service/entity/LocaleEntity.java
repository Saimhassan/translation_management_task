package com.translation.managment.service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "translation_locales", indexes = {
        @Index(name = "idx_locale_code", columnList = "code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;

}
