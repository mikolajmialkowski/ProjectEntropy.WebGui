package com.entropy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.entropy.IntegrationTest;
import com.entropy.domain.SchedulerSettings;
import com.entropy.repository.SchedulerSettingsRepository;
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
 * Integration tests for the {@link SchedulerSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SchedulerSettingsResourceIT {

    private static final String DEFAULT_INTERVAL = "AAAAAAAAAA";
    private static final String UPDATED_INTERVAL = "BBBBBBBBBB";

    private static final String DEFAULT_LIMIT = "AAAAAAAAAA";
    private static final String UPDATED_LIMIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/scheduler-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchedulerSettingsRepository schedulerSettingsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchedulerSettingsMockMvc;

    private SchedulerSettings schedulerSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerSettings createEntity(EntityManager em) {
        SchedulerSettings schedulerSettings = new SchedulerSettings().interval(DEFAULT_INTERVAL).limit(DEFAULT_LIMIT);
        return schedulerSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulerSettings createUpdatedEntity(EntityManager em) {
        SchedulerSettings schedulerSettings = new SchedulerSettings().interval(UPDATED_INTERVAL).limit(UPDATED_LIMIT);
        return schedulerSettings;
    }

    @BeforeEach
    public void initTest() {
        schedulerSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createSchedulerSettings() throws Exception {
        int databaseSizeBeforeCreate = schedulerSettingsRepository.findAll().size();
        // Create the SchedulerSettings
        restSchedulerSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulerSettings))
            )
            .andExpect(status().isCreated());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        SchedulerSettings testSchedulerSettings = schedulerSettingsList.get(schedulerSettingsList.size() - 1);
        assertThat(testSchedulerSettings.getInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testSchedulerSettings.getLimit()).isEqualTo(DEFAULT_LIMIT);
    }

    @Test
    @Transactional
    void createSchedulerSettingsWithExistingId() throws Exception {
        // Create the SchedulerSettings with an existing ID
        schedulerSettings.setId(1L);

        int databaseSizeBeforeCreate = schedulerSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulerSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulerSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSchedulerSettings() throws Exception {
        // Initialize the database
        schedulerSettingsRepository.saveAndFlush(schedulerSettings);

        // Get all the schedulerSettingsList
        restSchedulerSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulerSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].interval").value(hasItem(DEFAULT_INTERVAL)))
            .andExpect(jsonPath("$.[*].limit").value(hasItem(DEFAULT_LIMIT)));
    }

    @Test
    @Transactional
    void getSchedulerSettings() throws Exception {
        // Initialize the database
        schedulerSettingsRepository.saveAndFlush(schedulerSettings);

        // Get the schedulerSettings
        restSchedulerSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, schedulerSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedulerSettings.getId().intValue()))
            .andExpect(jsonPath("$.interval").value(DEFAULT_INTERVAL))
            .andExpect(jsonPath("$.limit").value(DEFAULT_LIMIT));
    }

    @Test
    @Transactional
    void getNonExistingSchedulerSettings() throws Exception {
        // Get the schedulerSettings
        restSchedulerSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchedulerSettings() throws Exception {
        // Initialize the database
        schedulerSettingsRepository.saveAndFlush(schedulerSettings);

        int databaseSizeBeforeUpdate = schedulerSettingsRepository.findAll().size();

        // Update the schedulerSettings
        SchedulerSettings updatedSchedulerSettings = schedulerSettingsRepository.findById(schedulerSettings.getId()).get();
        // Disconnect from session so that the updates on updatedSchedulerSettings are not directly saved in db
        em.detach(updatedSchedulerSettings);
        updatedSchedulerSettings.interval(UPDATED_INTERVAL).limit(UPDATED_LIMIT);

        restSchedulerSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSchedulerSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSchedulerSettings))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerSettings testSchedulerSettings = schedulerSettingsList.get(schedulerSettingsList.size() - 1);
        assertThat(testSchedulerSettings.getInterval()).isEqualTo(UPDATED_INTERVAL);
        assertThat(testSchedulerSettings.getLimit()).isEqualTo(UPDATED_LIMIT);
    }

    @Test
    @Transactional
    void putNonExistingSchedulerSettings() throws Exception {
        int databaseSizeBeforeUpdate = schedulerSettingsRepository.findAll().size();
        schedulerSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulerSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedulerSettings() throws Exception {
        int databaseSizeBeforeUpdate = schedulerSettingsRepository.findAll().size();
        schedulerSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulerSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedulerSettings() throws Exception {
        int databaseSizeBeforeUpdate = schedulerSettingsRepository.findAll().size();
        schedulerSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerSettingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(schedulerSettings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchedulerSettingsWithPatch() throws Exception {
        // Initialize the database
        schedulerSettingsRepository.saveAndFlush(schedulerSettings);

        int databaseSizeBeforeUpdate = schedulerSettingsRepository.findAll().size();

        // Update the schedulerSettings using partial update
        SchedulerSettings partialUpdatedSchedulerSettings = new SchedulerSettings();
        partialUpdatedSchedulerSettings.setId(schedulerSettings.getId());

        partialUpdatedSchedulerSettings.interval(UPDATED_INTERVAL);

        restSchedulerSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerSettings))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerSettings testSchedulerSettings = schedulerSettingsList.get(schedulerSettingsList.size() - 1);
        assertThat(testSchedulerSettings.getInterval()).isEqualTo(UPDATED_INTERVAL);
        assertThat(testSchedulerSettings.getLimit()).isEqualTo(DEFAULT_LIMIT);
    }

    @Test
    @Transactional
    void fullUpdateSchedulerSettingsWithPatch() throws Exception {
        // Initialize the database
        schedulerSettingsRepository.saveAndFlush(schedulerSettings);

        int databaseSizeBeforeUpdate = schedulerSettingsRepository.findAll().size();

        // Update the schedulerSettings using partial update
        SchedulerSettings partialUpdatedSchedulerSettings = new SchedulerSettings();
        partialUpdatedSchedulerSettings.setId(schedulerSettings.getId());

        partialUpdatedSchedulerSettings.interval(UPDATED_INTERVAL).limit(UPDATED_LIMIT);

        restSchedulerSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulerSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulerSettings))
            )
            .andExpect(status().isOk());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeUpdate);
        SchedulerSettings testSchedulerSettings = schedulerSettingsList.get(schedulerSettingsList.size() - 1);
        assertThat(testSchedulerSettings.getInterval()).isEqualTo(UPDATED_INTERVAL);
        assertThat(testSchedulerSettings.getLimit()).isEqualTo(UPDATED_LIMIT);
    }

    @Test
    @Transactional
    void patchNonExistingSchedulerSettings() throws Exception {
        int databaseSizeBeforeUpdate = schedulerSettingsRepository.findAll().size();
        schedulerSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulerSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedulerSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedulerSettings() throws Exception {
        int databaseSizeBeforeUpdate = schedulerSettingsRepository.findAll().size();
        schedulerSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedulerSettings() throws Exception {
        int databaseSizeBeforeUpdate = schedulerSettingsRepository.findAll().size();
        schedulerSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulerSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulerSettings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulerSettings in the database
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchedulerSettings() throws Exception {
        // Initialize the database
        schedulerSettingsRepository.saveAndFlush(schedulerSettings);

        int databaseSizeBeforeDelete = schedulerSettingsRepository.findAll().size();

        // Delete the schedulerSettings
        restSchedulerSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedulerSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchedulerSettings> schedulerSettingsList = schedulerSettingsRepository.findAll();
        assertThat(schedulerSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
