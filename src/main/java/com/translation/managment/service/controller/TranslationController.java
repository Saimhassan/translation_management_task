package com.translation.managment.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.translation.managment.service.dto.TranslationDTO;
import com.translation.managment.service.factory.TranslationDataFactory;
import com.translation.managment.service.service.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/translations")
@Tag(name = "Translations", description = "Manage translation entries")
public class TranslationController {

    private final TranslationService service;
    private final ObjectMapper objectMapper;
    private final TranslationDataFactory factory;

    public TranslationController(TranslationService service, ObjectMapper objectMapper,TranslationDataFactory factory) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.factory = factory;
    }

    @Operation(summary = "Create a new translation", responses = {
            @ApiResponse(responseCode = "200", description = "Translation created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TranslationDTO translation) {
        try {
            TranslationDTO created = service.create(translation);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create translation: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + ex.getMessage());
        }
    }

    @Operation(summary = "Update translation by ID")
    @PutMapping("/update/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TranslationDTO> update(@PathVariable Long id, @RequestBody TranslationDTO translation) {
        return ResponseEntity.ok(service.update(id, translation));
    }

    @Operation(summary = "Get translation by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TranslationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.of(service.getById(id));
    }

    @Operation(summary = "Get all translations (paginated)")
    @GetMapping
    public ResponseEntity<Page<TranslationDTO>> findAll(
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        Page<TranslationDTO> page = service.getAllTranslations(pageable);
        return page.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(page);
    }

    @Operation(summary = "Search translations by key, content, or tag")
    @GetMapping("/search")
    public ResponseEntity<List<TranslationDTO>> searchTranslations(
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String tag
    ) {
        List<TranslationDTO> results = service.searchTranslations(key, content, tag);
        return results.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(results);
    }

//    @Operation(summary = "Export all translations as JSON file")
//    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void exportTranslations(HttpServletResponse response) throws IOException {
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=translations.json");
//        response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");
//
//        try (ServletOutputStream out = response.getOutputStream()) {
//            service.exportAllTranslations(out, objectMapper);
//        }
//    }

    @Operation(summary = "Export all translations as JSON file")
    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public void exportTranslations(HttpServletResponse response, @RequestParam("localeCode") String localeCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=translations.json");
        response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");

        response.flushBuffer();

        try (ServletOutputStream out = response.getOutputStream()) {
            service.exportAllTranslations(out, objectMapper,localeCode);
        }
    }

    @Operation(summary = "Generate mock translation data")
    @PostMapping("/translations")
    public ResponseEntity<?> generateTranslations() {
        factory.generateTranslations();
        return ResponseEntity.ok("Translations inserted.");
    }

}
