package com.entropy.web.rest;

import com.entropy.domain.Prediction;
import com.entropy.repository.PredictionRepository;
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
 * REST controller for managing {@link com.entropy.domain.Prediction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PredictionResource {

    private final Logger log = LoggerFactory.getLogger(PredictionResource.class);

    private static final String ENTITY_NAME = "prediction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PredictionRepository predictionRepository;

    public PredictionResource(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    /**
     * {@code POST  /predictions} : Create a new prediction.
     *
     * @param prediction the prediction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prediction, or with status {@code 400 (Bad Request)} if the prediction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/predictions")
    public ResponseEntity<Prediction> createPrediction(@RequestBody Prediction prediction) throws URISyntaxException {
        log.debug("REST request to save Prediction : {}", prediction);
        if (prediction.getId() != null) {
            throw new BadRequestAlertException("A new prediction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prediction result = predictionRepository.save(prediction);
        return ResponseEntity
            .created(new URI("/api/predictions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /predictions/:id} : Updates an existing prediction.
     *
     * @param id the id of the prediction to save.
     * @param prediction the prediction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prediction,
     * or with status {@code 400 (Bad Request)} if the prediction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prediction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/predictions/{id}")
    public ResponseEntity<Prediction> updatePrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Prediction prediction
    ) throws URISyntaxException {
        log.debug("REST request to update Prediction : {}, {}", id, prediction);
        if (prediction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prediction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!predictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prediction result = predictionRepository.save(prediction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prediction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /predictions/:id} : Partial updates given fields of an existing prediction, field will ignore if it is null
     *
     * @param id the id of the prediction to save.
     * @param prediction the prediction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prediction,
     * or with status {@code 400 (Bad Request)} if the prediction is not valid,
     * or with status {@code 404 (Not Found)} if the prediction is not found,
     * or with status {@code 500 (Internal Server Error)} if the prediction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/predictions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Prediction> partialUpdatePrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Prediction prediction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prediction partially : {}, {}", id, prediction);
        if (prediction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prediction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!predictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prediction> result = predictionRepository
            .findById(prediction.getId())
            .map(existingPrediction -> {
                if (prediction.getDateFrom() != null) {
                    existingPrediction.setDateFrom(prediction.getDateFrom());
                }
                if (prediction.getDateTo() != null) {
                    existingPrediction.setDateTo(prediction.getDateTo());
                }
                if (prediction.getDuration() != null) {
                    existingPrediction.setDuration(prediction.getDuration());
                }
                if (prediction.getCurrentPrice() != null) {
                    existingPrediction.setCurrentPrice(prediction.getCurrentPrice());
                }
                if (prediction.getPredictedPraice() != null) {
                    existingPrediction.setPredictedPraice(prediction.getPredictedPraice());
                }
                if (prediction.getProbability() != null) {
                    existingPrediction.setProbability(prediction.getProbability());
                }

                return existingPrediction;
            })
            .map(predictionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prediction.getId().toString())
        );
    }

    /**
     * {@code GET  /predictions} : get all the predictions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of predictions in body.
     */
    @GetMapping("/predictions")
    public List<Prediction> getAllPredictions() {
        log.debug("REST request to get all Predictions");
        return predictionRepository.findAll();
    }

    /**
     * {@code GET  /predictions/:id} : get the "id" prediction.
     *
     * @param id the id of the prediction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prediction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/predictions/{id}")
    public ResponseEntity<Prediction> getPrediction(@PathVariable Long id) {
        log.debug("REST request to get Prediction : {}", id);
        Optional<Prediction> prediction = predictionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(prediction);
    }

    /**
     * {@code DELETE  /predictions/:id} : delete the "id" prediction.
     *
     * @param id the id of the prediction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/predictions/{id}")
    public ResponseEntity<Void> deletePrediction(@PathVariable Long id) {
        log.debug("REST request to delete Prediction : {}", id);
        predictionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
