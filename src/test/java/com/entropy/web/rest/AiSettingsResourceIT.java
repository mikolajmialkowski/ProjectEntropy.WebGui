package com.entropy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.entropy.IntegrationTest;
import com.entropy.domain.AiSettings;
import com.entropy.repository.AiSettingsRepository;
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
 * Integration tests for the {@link AiSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AiSettingsResourceIT {

    private static final String ENTITY_API_URL = "/api/ai-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AiSettingsRepository aiSettingsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAiSettingsMockMvc;

    private AiSettings aiSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AiSettings createEntity(EntityManager em) {
        AiSettings aiSettings = new AiSettings();
        return aiSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AiSettings createUpdatedEntity(EntityManager em) {
        AiSettings aiSettings = new AiSettings();
        return aiSettings;
    }

    @BeforeEach
    public void initTest() {
        aiSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createAiSettings() throws Exception {
        int databaseSizeBeforeCreate = aiSettingsRepository.findAll().size();
        // Create the AiSettings
        restAiSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aiSettings)))
            .andExpect(status().isCreated());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        AiSettings testAiSettings = aiSettingsList.get(aiSettingsList.size() - 1);
    }

    @Test
    @Transactional
    void createAiSettingsWithExistingId() throws Exception {
        // Create the AiSettings with an existing ID
        aiSettings.setId(1L);

        int databaseSizeBeforeCreate = aiSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAiSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aiSettings)))
            .andExpect(status().isBadRequest());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAiSettings() throws Exception {
        // Initialize the database
        aiSettingsRepository.saveAndFlush(aiSettings);

        // Get all the aiSettingsList
        restAiSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aiSettings.getId().intValue())));
    }

    @Test
    @Transactional
    void getAiSettings() throws Exception {
        // Initialize the database
        aiSettingsRepository.saveAndFlush(aiSettings);

        // Get the aiSettings
        restAiSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, aiSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aiSettings.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAiSettings() throws Exception {
        // Get the aiSettings
        restAiSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAiSettings() throws Exception {
        // Initialize the database
        aiSettingsRepository.saveAndFlush(aiSettings);

        int databaseSizeBeforeUpdate = aiSettingsRepository.findAll().size();

        // Update the aiSettings
        AiSettings updatedAiSettings = aiSettingsRepository.findById(aiSettings.getId()).get();
        // Disconnect from session so that the updates on updatedAiSettings are not directly saved in db
        em.detach(updatedAiSettings);

        restAiSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAiSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAiSettings))
            )
            .andExpect(status().isOk());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeUpdate);
        AiSettings testAiSettings = aiSettingsList.get(aiSettingsList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingAiSettings() throws Exception {
        int databaseSizeBeforeUpdate = aiSettingsRepository.findAll().size();
        aiSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAiSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aiSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aiSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAiSettings() throws Exception {
        int databaseSizeBeforeUpdate = aiSettingsRepository.findAll().size();
        aiSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAiSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aiSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAiSettings() throws Exception {
        int databaseSizeBeforeUpdate = aiSettingsRepository.findAll().size();
        aiSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAiSettingsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aiSettings)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAiSettingsWithPatch() throws Exception {
        // Initialize the database
        aiSettingsRepository.saveAndFlush(aiSettings);

        int databaseSizeBeforeUpdate = aiSettingsRepository.findAll().size();

        // Update the aiSettings using partial update
        AiSettings partialUpdatedAiSettings = new AiSettings();
        partialUpdatedAiSettings.setId(aiSettings.getId());

        restAiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAiSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAiSettings))
            )
            .andExpect(status().isOk());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeUpdate);
        AiSettings testAiSettings = aiSettingsList.get(aiSettingsList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateAiSettingsWithPatch() throws Exception {
        // Initialize the database
        aiSettingsRepository.saveAndFlush(aiSettings);

        int databaseSizeBeforeUpdate = aiSettingsRepository.findAll().size();

        // Update the aiSettings using partial update
        AiSettings partialUpdatedAiSettings = new AiSettings();
        partialUpdatedAiSettings.setId(aiSettings.getId());

        restAiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAiSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAiSettings))
            )
            .andExpect(status().isOk());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeUpdate);
        AiSettings testAiSettings = aiSettingsList.get(aiSettingsList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingAiSettings() throws Exception {
        int databaseSizeBeforeUpdate = aiSettingsRepository.findAll().size();
        aiSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aiSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aiSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAiSettings() throws Exception {
        int databaseSizeBeforeUpdate = aiSettingsRepository.findAll().size();
        aiSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aiSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAiSettings() throws Exception {
        int databaseSizeBeforeUpdate = aiSettingsRepository.findAll().size();
        aiSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aiSettings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AiSettings in the database
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAiSettings() throws Exception {
        // Initialize the database
        aiSettingsRepository.saveAndFlush(aiSettings);

        int databaseSizeBeforeDelete = aiSettingsRepository.findAll().size();

        // Delete the aiSettings
        restAiSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, aiSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AiSettings> aiSettingsList = aiSettingsRepository.findAll();
        assertThat(aiSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
