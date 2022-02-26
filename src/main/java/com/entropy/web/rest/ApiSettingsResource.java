package com.entropy.web.rest;

import com.entropy.domain.ApiSettings;
import com.entropy.repository.ApiSettingsRepository;
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
 * REST controller for managing {@link com.entropy.domain.ApiSettings}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApiSettingsResource {

    private final Logger log = LoggerFactory.getLogger(ApiSettingsResource.class);

    private static final String ENTITY_NAME = "apiSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApiSettingsRepository apiSettingsRepository;

    public ApiSettingsResource(ApiSettingsRepository apiSettingsRepository) {
        this.apiSettingsRepository = apiSettingsRepository;
    }

    /**
     * {@code POST  /api-settings} : Create a new apiSettings.
     *
     * @param apiSettings the apiSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apiSettings, or with status {@code 400 (Bad Request)} if the apiSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api-settings")
    public ResponseEntity<ApiSettings> createApiSettings(@RequestBody ApiSettings apiSettings) throws URISyntaxException {
        log.debug("REST request to save ApiSettings : {}", apiSettings);
        if (apiSettings.getId() != null) {
            throw new BadRequestAlertException("A new apiSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApiSettings result = apiSettingsRepository.save(apiSettings);
        return ResponseEntity
            .created(new URI("/api/api-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api-settings/:id} : Updates an existing apiSettings.
     *
     * @param id the id of the apiSettings to save.
     * @param apiSettings the apiSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiSettings,
     * or with status {@code 400 (Bad Request)} if the apiSettings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api-settings/{id}")
    public ResponseEntity<ApiSettings> updateApiSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApiSettings apiSettings
    ) throws URISyntaxException {
        log.debug("REST request to update ApiSettings : {}, {}", id, apiSettings);
        if (apiSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiSettings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApiSettings result = apiSettingsRepository.save(apiSettings);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiSettings.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /api-settings/:id} : Partial updates given fields of an existing apiSettings, field will ignore if it is null
     *
     * @param id the id of the apiSettings to save.
     * @param apiSettings the apiSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiSettings,
     * or with status {@code 400 (Bad Request)} if the apiSettings is not valid,
     * or with status {@code 404 (Not Found)} if the apiSettings is not found,
     * or with status {@code 500 (Internal Server Error)} if the apiSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/api-settings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApiSettings> partialUpdateApiSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApiSettings apiSettings
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApiSettings partially : {}, {}", id, apiSettings);
        if (apiSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiSettings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApiSettings> result = apiSettingsRepository
            .findById(apiSettings.getId())
            .map(existingApiSettings -> {
                if (apiSettings.getApiUri1() != null) {
                    existingApiSettings.setApiUri1(apiSettings.getApiUri1());
                }
                if (apiSettings.getApiUri2() != null) {
                    existingApiSettings.setApiUri2(apiSettings.getApiUri2());
                }
                if (apiSettings.getApiUri3() != null) {
                    existingApiSettings.setApiUri3(apiSettings.getApiUri3());
                }
                if (apiSettings.getApiToken() != null) {
                    existingApiSettings.setApiToken(apiSettings.getApiToken());
                }

                return existingApiSettings;
            })
            .map(apiSettingsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiSettings.getId().toString())
        );
    }

    /**
     * {@code GET  /api-settings} : get all the apiSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apiSettings in body.
     */
    @GetMapping("/api-settings")
    public List<ApiSettings> getAllApiSettings() {
        log.debug("REST request to get all ApiSettings");
        return apiSettingsRepository.findAll();
    }

    /**
     * {@code GET  /api-settings/:id} : get the "id" apiSettings.
     *
     * @param id the id of the apiSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apiSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api-settings/{id}")
    public ResponseEntity<ApiSettings> getApiSettings(@PathVariable Long id) {
        log.debug("REST request to get ApiSettings : {}", id);
        Optional<ApiSettings> apiSettings = apiSettingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apiSettings);
    }

    /**
     * {@code DELETE  /api-settings/:id} : delete the "id" apiSettings.
     *
     * @param id the id of the apiSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api-settings/{id}")
    public ResponseEntity<Void> deleteApiSettings(@PathVariable Long id) {
        log.debug("REST request to delete ApiSettings : {}", id);
        apiSettingsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
