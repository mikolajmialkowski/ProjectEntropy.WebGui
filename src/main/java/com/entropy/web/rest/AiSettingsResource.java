package com.entropy.web.rest;

import com.entropy.domain.AiSettings;
import com.entropy.repository.AiSettingsRepository;
import com.entropy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.entropy.domain.AiSettings}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AiSettingsResource {

    private final Logger log = LoggerFactory.getLogger(AiSettingsResource.class);

    private static final String ENTITY_NAME = "aiSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AiSettingsRepository aiSettingsRepository;

    public AiSettingsResource(AiSettingsRepository aiSettingsRepository) {
        this.aiSettingsRepository = aiSettingsRepository;
    }

    /**
     * {@code POST  /ai-settings} : Create a new aiSettings.
     *
     * @param aiSettings the aiSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aiSettings, or with status {@code 400 (Bad Request)} if the aiSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ai-settings")
    public ResponseEntity<AiSettings> createAiSettings(@RequestBody AiSettings aiSettings) throws URISyntaxException {
        log.debug("REST request to save AiSettings : {}", aiSettings);
        if (aiSettings.getId() != null) {
            throw new BadRequestAlertException("A new aiSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AiSettings result = aiSettingsRepository.save(aiSettings);
        return ResponseEntity
            .created(new URI("/api/ai-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ai-settings/:id} : Updates an existing aiSettings.
     *
     * @param id the id of the aiSettings to save.
     * @param aiSettings the aiSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aiSettings,
     * or with status {@code 400 (Bad Request)} if the aiSettings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aiSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ai-settings/{id}")
    public ResponseEntity<AiSettings> updateAiSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AiSettings aiSettings
    ) throws URISyntaxException {
        log.debug("REST request to update AiSettings : {}, {}", id, aiSettings);
        if (aiSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aiSettings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aiSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AiSettings result = aiSettingsRepository.save(aiSettings);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aiSettings.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ai-settings/:id} : Partial updates given fields of an existing aiSettings, field will ignore if it is null
     *
     * @param id the id of the aiSettings to save.
     * @param aiSettings the aiSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aiSettings,
     * or with status {@code 400 (Bad Request)} if the aiSettings is not valid,
     * or with status {@code 404 (Not Found)} if the aiSettings is not found,
     * or with status {@code 500 (Internal Server Error)} if the aiSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ai-settings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AiSettings> partialUpdateAiSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AiSettings aiSettings
    ) throws URISyntaxException {
        log.debug("REST request to partial update AiSettings partially : {}, {}", id, aiSettings);
        if (aiSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aiSettings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aiSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AiSettings> result = aiSettingsRepository
            .findById(aiSettings.getId())
            .map(existingAiSettings -> {
                return existingAiSettings;
            })
            .map(aiSettingsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aiSettings.getId().toString())
        );
    }

    /**
     * {@code GET  /ai-settings} : get all the aiSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aiSettings in body.
     */
    @GetMapping("/ai-settings")
    public List<AiSettings> getAllAiSettings() {
        log.debug("REST request to get all AiSettings");
        return aiSettingsRepository.findAll();
    }

    /**
     * {@code GET  /ai-settings/:id} : get the "id" aiSettings.
     *
     * @param id the id of the aiSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aiSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ai-settings/{id}")
    public ResponseEntity<AiSettings> getAiSettings(@PathVariable Long id) {
        log.debug("REST request to get AiSettings : {}", id);
        Optional<AiSettings> aiSettings = aiSettingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aiSettings);
    }

    /**
     * {@code DELETE  /ai-settings/:id} : delete the "id" aiSettings.
     *
     * @param id the id of the aiSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ai-settings/{id}")
    public ResponseEntity<Void> deleteAiSettings(@PathVariable Long id) {
        log.debug("REST request to delete AiSettings : {}", id);
        aiSettingsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
