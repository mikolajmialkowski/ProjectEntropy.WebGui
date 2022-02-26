package com.entropy.web.rest;

import com.entropy.domain.Cryptocurrency;
import com.entropy.repository.CryptocurrencyRepository;
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
 * REST controller for managing {@link com.entropy.domain.Cryptocurrency}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CryptocurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CryptocurrencyResource.class);

    private static final String ENTITY_NAME = "cryptocurrency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CryptocurrencyRepository cryptocurrencyRepository;

    public CryptocurrencyResource(CryptocurrencyRepository cryptocurrencyRepository) {
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    /**
     * {@code POST  /cryptocurrencies} : Create a new cryptocurrency.
     *
     * @param cryptocurrency the cryptocurrency to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cryptocurrency, or with status {@code 400 (Bad Request)} if the cryptocurrency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cryptocurrencies")
    public ResponseEntity<Cryptocurrency> createCryptocurrency(@RequestBody Cryptocurrency cryptocurrency) throws URISyntaxException {
        log.debug("REST request to save Cryptocurrency : {}", cryptocurrency);
        if (cryptocurrency.getId() != null) {
            throw new BadRequestAlertException("A new cryptocurrency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cryptocurrency result = cryptocurrencyRepository.save(cryptocurrency);
        return ResponseEntity
            .created(new URI("/api/cryptocurrencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cryptocurrencies/:id} : Updates an existing cryptocurrency.
     *
     * @param id the id of the cryptocurrency to save.
     * @param cryptocurrency the cryptocurrency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cryptocurrency,
     * or with status {@code 400 (Bad Request)} if the cryptocurrency is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cryptocurrency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cryptocurrencies/{id}")
    public ResponseEntity<Cryptocurrency> updateCryptocurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cryptocurrency cryptocurrency
    ) throws URISyntaxException {
        log.debug("REST request to update Cryptocurrency : {}, {}", id, cryptocurrency);
        if (cryptocurrency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cryptocurrency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cryptocurrencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cryptocurrency result = cryptocurrencyRepository.save(cryptocurrency);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cryptocurrency.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cryptocurrencies/:id} : Partial updates given fields of an existing cryptocurrency, field will ignore if it is null
     *
     * @param id the id of the cryptocurrency to save.
     * @param cryptocurrency the cryptocurrency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cryptocurrency,
     * or with status {@code 400 (Bad Request)} if the cryptocurrency is not valid,
     * or with status {@code 404 (Not Found)} if the cryptocurrency is not found,
     * or with status {@code 500 (Internal Server Error)} if the cryptocurrency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cryptocurrencies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cryptocurrency> partialUpdateCryptocurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cryptocurrency cryptocurrency
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cryptocurrency partially : {}, {}", id, cryptocurrency);
        if (cryptocurrency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cryptocurrency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cryptocurrencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cryptocurrency> result = cryptocurrencyRepository
            .findById(cryptocurrency.getId())
            .map(existingCryptocurrency -> {
                if (cryptocurrency.getName() != null) {
                    existingCryptocurrency.setName(cryptocurrency.getName());
                }
                if (cryptocurrency.getPair() != null) {
                    existingCryptocurrency.setPair(cryptocurrency.getPair());
                }
                if (cryptocurrency.getSymbol() != null) {
                    existingCryptocurrency.setSymbol(cryptocurrency.getSymbol());
                }
                if (cryptocurrency.getPrice() != null) {
                    existingCryptocurrency.setPrice(cryptocurrency.getPrice());
                }

                return existingCryptocurrency;
            })
            .map(cryptocurrencyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cryptocurrency.getId().toString())
        );
    }

    /**
     * {@code GET  /cryptocurrencies} : get all the cryptocurrencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cryptocurrencies in body.
     */
    @GetMapping("/cryptocurrencies")
    public List<Cryptocurrency> getAllCryptocurrencies() {
        log.debug("REST request to get all Cryptocurrencies");
        return cryptocurrencyRepository.findAll();
    }

    /**
     * {@code GET  /cryptocurrencies/:id} : get the "id" cryptocurrency.
     *
     * @param id the id of the cryptocurrency to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cryptocurrency, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cryptocurrencies/{id}")
    public ResponseEntity<Cryptocurrency> getCryptocurrency(@PathVariable Long id) {
        log.debug("REST request to get Cryptocurrency : {}", id);
        Optional<Cryptocurrency> cryptocurrency = cryptocurrencyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cryptocurrency);
    }

    /**
     * {@code DELETE  /cryptocurrencies/:id} : delete the "id" cryptocurrency.
     *
     * @param id the id of the cryptocurrency to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cryptocurrencies/{id}")
    public ResponseEntity<Void> deleteCryptocurrency(@PathVariable Long id) {
        log.debug("REST request to delete Cryptocurrency : {}", id);
        cryptocurrencyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
