package com.translation.managment.service.controller;

import com.translation.managment.service.dto.LocaleDTO;
import com.translation.managment.service.service.LocaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/locale")
@Tag(name = "Locale", description = "Manage supported locales")
public class LocaleController {

    @Autowired
    LocaleService localeService;

    @Operation(summary = "Create a new locale")
    @PostMapping("/create")
    public ResponseEntity<LocaleDTO> create(@RequestBody LocaleDTO translation) {
        return ResponseEntity.ok(localeService.createLocale(translation));
    }

}
