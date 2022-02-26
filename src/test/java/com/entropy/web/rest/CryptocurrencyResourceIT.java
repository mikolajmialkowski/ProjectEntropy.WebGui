package com.entropy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.entropy.IntegrationTest;
import com.entropy.domain.Cryptocurrency;
import com.entropy.repository.CryptocurrencyRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CryptocurrencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CryptocurrencyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAIR = "AAAAAAAAAA";
    private static final String UPDATED_PAIR = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cryptocurrencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCryptocurrencyMockMvc;

    private Cryptocurrency cryptocurrency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cryptocurrency createEntity(EntityManager em) {
        Cryptocurrency cryptocurrency = new Cryptocurrency()
            .name(DEFAULT_NAME)
            .pair(DEFAULT_PAIR)
            .symbol(DEFAULT_SYMBOL)
            .price(DEFAULT_PRICE);
        return cryptocurrency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cryptocurrency createUpdatedEntity(EntityManager em) {
        Cryptocurrency cryptocurrency = new Cryptocurrency()
            .name(UPDATED_NAME)
            .pair(UPDATED_PAIR)
            .symbol(UPDATED_SYMBOL)
            .price(UPDATED_PRICE);
        return cryptocurrency;
    }

    @BeforeEach
    public void initTest() {
        cryptocurrency = createEntity(em);
    }

    @Test
    @Transactional
    void createCryptocurrency() throws Exception {
        int databaseSizeBeforeCreate = cryptocurrencyRepository.findAll().size();
        // Create the Cryptocurrency
        restCryptocurrencyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cryptocurrency))
            )
            .andExpect(status().isCreated());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeCreate + 1);
        Cryptocurrency testCryptocurrency = cryptocurrencyList.get(cryptocurrencyList.size() - 1);
        assertThat(testCryptocurrency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCryptocurrency.getPair()).isEqualTo(DEFAULT_PAIR);
        assertThat(testCryptocurrency.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testCryptocurrency.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createCryptocurrencyWithExistingId() throws Exception {
        // Create the Cryptocurrency with an existing ID
        cryptocurrency.setId(1L);

        int databaseSizeBeforeCreate = cryptocurrencyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCryptocurrencyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cryptocurrency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCryptocurrencies() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        // Get all the cryptocurrencyList
        restCryptocurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cryptocurrency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pair").value(hasItem(DEFAULT_PAIR)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getCryptocurrency() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        // Get the cryptocurrency
        restCryptocurrencyMockMvc
            .perform(get(ENTITY_API_URL_ID, cryptocurrency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cryptocurrency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pair").value(DEFAULT_PAIR))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE));
    }

    @Test
    @Transactional
    void getNonExistingCryptocurrency() throws Exception {
        // Get the cryptocurrency
        restCryptocurrencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCryptocurrency() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();

        // Update the cryptocurrency
        Cryptocurrency updatedCryptocurrency = cryptocurrencyRepository.findById(cryptocurrency.getId()).get();
        // Disconnect from session so that the updates on updatedCryptocurrency are not directly saved in db
        em.detach(updatedCryptocurrency);
        updatedCryptocurrency.name(UPDATED_NAME).pair(UPDATED_PAIR).symbol(UPDATED_SYMBOL).price(UPDATED_PRICE);

        restCryptocurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCryptocurrency.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCryptocurrency))
            )
            .andExpect(status().isOk());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
        Cryptocurrency testCryptocurrency = cryptocurrencyList.get(cryptocurrencyList.size() - 1);
        assertThat(testCryptocurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCryptocurrency.getPair()).isEqualTo(UPDATED_PAIR);
        assertThat(testCryptocurrency.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testCryptocurrency.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingCryptocurrency() throws Exception {
        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();
        cryptocurrency.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCryptocurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cryptocurrency.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cryptocurrency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCryptocurrency() throws Exception {
        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();
        cryptocurrency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCryptocurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cryptocurrency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCryptocurrency() throws Exception {
        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();
        cryptocurrency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCryptocurrencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cryptocurrency)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCryptocurrencyWithPatch() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();

        // Update the cryptocurrency using partial update
        Cryptocurrency partialUpdatedCryptocurrency = new Cryptocurrency();
        partialUpdatedCryptocurrency.setId(cryptocurrency.getId());

        partialUpdatedCryptocurrency.name(UPDATED_NAME).symbol(UPDATED_SYMBOL).price(UPDATED_PRICE);

        restCryptocurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCryptocurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCryptocurrency))
            )
            .andExpect(status().isOk());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
        Cryptocurrency testCryptocurrency = cryptocurrencyList.get(cryptocurrencyList.size() - 1);
        assertThat(testCryptocurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCryptocurrency.getPair()).isEqualTo(DEFAULT_PAIR);
        assertThat(testCryptocurrency.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testCryptocurrency.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateCryptocurrencyWithPatch() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();

        // Update the cryptocurrency using partial update
        Cryptocurrency partialUpdatedCryptocurrency = new Cryptocurrency();
        partialUpdatedCryptocurrency.setId(cryptocurrency.getId());

        partialUpdatedCryptocurrency.name(UPDATED_NAME).pair(UPDATED_PAIR).symbol(UPDATED_SYMBOL).price(UPDATED_PRICE);

        restCryptocurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCryptocurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCryptocurrency))
            )
            .andExpect(status().isOk());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
        Cryptocurrency testCryptocurrency = cryptocurrencyList.get(cryptocurrencyList.size() - 1);
        assertThat(testCryptocurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCryptocurrency.getPair()).isEqualTo(UPDATED_PAIR);
        assertThat(testCryptocurrency.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testCryptocurrency.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingCryptocurrency() throws Exception {
        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();
        cryptocurrency.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCryptocurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cryptocurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cryptocurrency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCryptocurrency() throws Exception {
        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();
        cryptocurrency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCryptocurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cryptocurrency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCryptocurrency() throws Exception {
        int databaseSizeBeforeUpdate = cryptocurrencyRepository.findAll().size();
        cryptocurrency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCryptocurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cryptocurrency))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cryptocurrency in the database
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCryptocurrency() throws Exception {
        // Initialize the database
        cryptocurrencyRepository.saveAndFlush(cryptocurrency);

        int databaseSizeBeforeDelete = cryptocurrencyRepository.findAll().size();

        // Delete the cryptocurrency
        restCryptocurrencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, cryptocurrency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cryptocurrency> cryptocurrencyList = cryptocurrencyRepository.findAll();
        assertThat(cryptocurrencyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
