package com.entropy.web.rest;

import com.entropy.domain.GeneralInfo;
import com.entropy.repository.GeneralInfoRepository;
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
 * REST controller for managing {@link com.entropy.domain.GeneralInfo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GeneralInfoResource {

    private final Logger log = LoggerFactory.getLogger(GeneralInfoResource.class);

    private static final String ENTITY_NAME = "generalInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeneralInfoRepository generalInfoRepository;

    public GeneralInfoResource(GeneralInfoRepository generalInfoRepository) {
        this.generalInfoRepository = generalInfoRepository;
    }

    /**
     * {@code POST  /general-infos} : Create a new generalInfo.
     *
     * @param generalInfo the generalInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new generalInfo, or with status {@code 400 (Bad Request)} if the generalInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/general-infos")
    public ResponseEntity<GeneralInfo> createGeneralInfo(@RequestBody GeneralInfo generalInfo) throws URISyntaxException {
        log.debug("REST request to save GeneralInfo : {}", generalInfo);
        if (generalInfo.getId() != null) {
            throw new BadRequestAlertException("A new generalInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeneralInfo result = generalInfoRepository.save(generalInfo);
        return ResponseEntity
            .created(new URI("/api/general-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /general-infos/:id} : Updates an existing generalInfo.
     *
     * @param id the id of the generalInfo to save.
     * @param generalInfo the generalInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generalInfo,
     * or with status {@code 400 (Bad Request)} if the generalInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the generalInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/general-infos/{id}")
    public ResponseEntity<GeneralInfo> updateGeneralInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GeneralInfo generalInfo
    ) throws URISyntaxException {
        log.debug("REST request to update GeneralInfo : {}, {}", id, generalInfo);
        if (generalInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generalInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generalInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GeneralInfo result = generalInfoRepository.save(generalInfo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, generalInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /general-infos/:id} : Partial updates given fields of an existing generalInfo, field will ignore if it is null
     *
     * @param id the id of the generalInfo to save.
     * @param generalInfo the generalInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generalInfo,
     * or with status {@code 400 (Bad Request)} if the generalInfo is not valid,
     * or with status {@code 404 (Not Found)} if the generalInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the generalInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/general-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GeneralInfo> partialUpdateGeneralInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GeneralInfo generalInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update GeneralInfo partially : {}, {}", id, generalInfo);
        if (generalInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generalInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generalInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GeneralInfo> result = generalInfoRepository
            .findById(generalInfo.getId())
            .map(existingGeneralInfo -> {
                if (generalInfo.getRecordsInDataBaseAmount() != null) {
                    existingGeneralInfo.setRecordsInDataBaseAmount(generalInfo.getRecordsInDataBaseAmount());
                }
                if (generalInfo.getApiCallsAmount() != null) {
                    existingGeneralInfo.setApiCallsAmount(generalInfo.getApiCallsAmount());
                }
                if (generalInfo.getApiStatistics() != null) {
                    existingGeneralInfo.setApiStatistics(generalInfo.getApiStatistics());
                }

                return existingGeneralInfo;
            })
            .map(generalInfoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, generalInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /general-infos} : get all the generalInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generalInfos in body.
     */
    @GetMapping("/general-infos")
    public List<GeneralInfo> getAllGeneralInfos() {
        log.debug("REST request to get all GeneralInfos");
        return generalInfoRepository.findAll();
    }

    /**
     * {@code GET  /general-infos/:id} : get the "id" generalInfo.
     *
     * @param id the id of the generalInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the generalInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/general-infos/{id}")
    public ResponseEntity<GeneralInfo> getGeneralInfo(@PathVariable Long id) {
        log.debug("REST request to get GeneralInfo : {}", id);
        Optional<GeneralInfo> generalInfo = generalInfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(generalInfo);
    }

    /**
     * {@code DELETE  /general-infos/:id} : delete the "id" generalInfo.
     *
     * @param id the id of the generalInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/general-infos/{id}")
    public ResponseEntity<Void> deleteGeneralInfo(@PathVariable Long id) {
        log.debug("REST request to delete GeneralInfo : {}", id);
        generalInfoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
