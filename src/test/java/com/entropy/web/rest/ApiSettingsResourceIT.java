package com.entropy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.entropy.IntegrationTest;
import com.entropy.domain.ApiSettings;
import com.entropy.repository.ApiSettingsRepository;
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
 * Integration tests for the {@link ApiSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApiSettingsResourceIT {

    private static final String DEFAULT_API_URI_1 = "AAAAAAAAAA";
    private static final String UPDATED_API_URI_1 = "BBBBBBBBBB";

    private static final String DEFAULT_API_URI_2 = "AAAAAAAAAA";
    private static final String UPDATED_API_URI_2 = "BBBBBBBBBB";

    private static final String DEFAULT_API_URI_3 = "AAAAAAAAAA";
    private static final String UPDATED_API_URI_3 = "BBBBBBBBBB";

    private static final String DEFAULT_API_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_API_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/api-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApiSettingsRepository apiSettingsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApiSettingsMockMvc;

    private ApiSettings apiSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiSettings createEntity(EntityManager em) {
        ApiSettings apiSettings = new ApiSettings()
            .apiUri1(DEFAULT_API_URI_1)
            .apiUri2(DEFAULT_API_URI_2)
            .apiUri3(DEFAULT_API_URI_3)
            .apiToken(DEFAULT_API_TOKEN);
        return apiSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiSettings createUpdatedEntity(EntityManager em) {
        ApiSettings apiSettings = new ApiSettings()
            .apiUri1(UPDATED_API_URI_1)
            .apiUri2(UPDATED_API_URI_2)
            .apiUri3(UPDATED_API_URI_3)
            .apiToken(UPDATED_API_TOKEN);
        return apiSettings;
    }

    @BeforeEach
    public void initTest() {
        apiSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createApiSettings() throws Exception {
        int databaseSizeBeforeCreate = apiSettingsRepository.findAll().size();
        // Create the ApiSettings
        restApiSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apiSettings)))
            .andExpect(status().isCreated());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        ApiSettings testApiSettings = apiSettingsList.get(apiSettingsList.size() - 1);
        assertThat(testApiSettings.getApiUri1()).isEqualTo(DEFAULT_API_URI_1);
        assertThat(testApiSettings.getApiUri2()).isEqualTo(DEFAULT_API_URI_2);
        assertThat(testApiSettings.getApiUri3()).isEqualTo(DEFAULT_API_URI_3);
        assertThat(testApiSettings.getApiToken()).isEqualTo(DEFAULT_API_TOKEN);
    }

    @Test
    @Transactional
    void createApiSettingsWithExistingId() throws Exception {
        // Create the ApiSettings with an existing ID
        apiSettings.setId(1L);

        int databaseSizeBeforeCreate = apiSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apiSettings)))
            .andExpect(status().isBadRequest());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApiSettings() throws Exception {
        // Initialize the database
        apiSettingsRepository.saveAndFlush(apiSettings);

        // Get all the apiSettingsList
        restApiSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].apiUri1").value(hasItem(DEFAULT_API_URI_1)))
            .andExpect(jsonPath("$.[*].apiUri2").value(hasItem(DEFAULT_API_URI_2)))
            .andExpect(jsonPath("$.[*].apiUri3").value(hasItem(DEFAULT_API_URI_3)))
            .andExpect(jsonPath("$.[*].apiToken").value(hasItem(DEFAULT_API_TOKEN)));
    }

    @Test
    @Transactional
    void getApiSettings() throws Exception {
        // Initialize the database
        apiSettingsRepository.saveAndFlush(apiSettings);

        // Get the apiSettings
        restApiSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, apiSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apiSettings.getId().intValue()))
            .andExpect(jsonPath("$.apiUri1").value(DEFAULT_API_URI_1))
            .andExpect(jsonPath("$.apiUri2").value(DEFAULT_API_URI_2))
            .andExpect(jsonPath("$.apiUri3").value(DEFAULT_API_URI_3))
            .andExpect(jsonPath("$.apiToken").value(DEFAULT_API_TOKEN));
    }

    @Test
    @Transactional
    void getNonExistingApiSettings() throws Exception {
        // Get the apiSettings
        restApiSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApiSettings() throws Exception {
        // Initialize the database
        apiSettingsRepository.saveAndFlush(apiSettings);

        int databaseSizeBeforeUpdate = apiSettingsRepository.findAll().size();

        // Update the apiSettings
        ApiSettings updatedApiSettings = apiSettingsRepository.findById(apiSettings.getId()).get();
        // Disconnect from session so that the updates on updatedApiSettings are not directly saved in db
        em.detach(updatedApiSettings);
        updatedApiSettings.apiUri1(UPDATED_API_URI_1).apiUri2(UPDATED_API_URI_2).apiUri3(UPDATED_API_URI_3).apiToken(UPDATED_API_TOKEN);

        restApiSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApiSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApiSettings))
            )
            .andExpect(status().isOk());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeUpdate);
        ApiSettings testApiSettings = apiSettingsList.get(apiSettingsList.size() - 1);
        assertThat(testApiSettings.getApiUri1()).isEqualTo(UPDATED_API_URI_1);
        assertThat(testApiSettings.getApiUri2()).isEqualTo(UPDATED_API_URI_2);
        assertThat(testApiSettings.getApiUri3()).isEqualTo(UPDATED_API_URI_3);
        assertThat(testApiSettings.getApiToken()).isEqualTo(UPDATED_API_TOKEN);
    }

    @Test
    @Transactional
    void putNonExistingApiSettings() throws Exception {
        int databaseSizeBeforeUpdate = apiSettingsRepository.findAll().size();
        apiSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apiSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApiSettings() throws Exception {
        int databaseSizeBeforeUpdate = apiSettingsRepository.findAll().size();
        apiSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apiSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApiSettings() throws Exception {
        int databaseSizeBeforeUpdate = apiSettingsRepository.findAll().size();
        apiSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiSettingsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apiSettings)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApiSettingsWithPatch() throws Exception {
        // Initialize the database
        apiSettingsRepository.saveAndFlush(apiSettings);

        int databaseSizeBeforeUpdate = apiSettingsRepository.findAll().size();

        // Update the apiSettings using partial update
        ApiSettings partialUpdatedApiSettings = new ApiSettings();
        partialUpdatedApiSettings.setId(apiSettings.getId());

        partialUpdatedApiSettings.apiUri1(UPDATED_API_URI_1).apiToken(UPDATED_API_TOKEN);

        restApiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApiSettings))
            )
            .andExpect(status().isOk());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeUpdate);
        ApiSettings testApiSettings = apiSettingsList.get(apiSettingsList.size() - 1);
        assertThat(testApiSettings.getApiUri1()).isEqualTo(UPDATED_API_URI_1);
        assertThat(testApiSettings.getApiUri2()).isEqualTo(DEFAULT_API_URI_2);
        assertThat(testApiSettings.getApiUri3()).isEqualTo(DEFAULT_API_URI_3);
        assertThat(testApiSettings.getApiToken()).isEqualTo(UPDATED_API_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateApiSettingsWithPatch() throws Exception {
        // Initialize the database
        apiSettingsRepository.saveAndFlush(apiSettings);

        int databaseSizeBeforeUpdate = apiSettingsRepository.findAll().size();

        // Update the apiSettings using partial update
        ApiSettings partialUpdatedApiSettings = new ApiSettings();
        partialUpdatedApiSettings.setId(apiSettings.getId());

        partialUpdatedApiSettings
            .apiUri1(UPDATED_API_URI_1)
            .apiUri2(UPDATED_API_URI_2)
            .apiUri3(UPDATED_API_URI_3)
            .apiToken(UPDATED_API_TOKEN);

        restApiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApiSettings))
            )
            .andExpect(status().isOk());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeUpdate);
        ApiSettings testApiSettings = apiSettingsList.get(apiSettingsList.size() - 1);
        assertThat(testApiSettings.getApiUri1()).isEqualTo(UPDATED_API_URI_1);
        assertThat(testApiSettings.getApiUri2()).isEqualTo(UPDATED_API_URI_2);
        assertThat(testApiSettings.getApiUri3()).isEqualTo(UPDATED_API_URI_3);
        assertThat(testApiSettings.getApiToken()).isEqualTo(UPDATED_API_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingApiSettings() throws Exception {
        int databaseSizeBeforeUpdate = apiSettingsRepository.findAll().size();
        apiSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apiSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apiSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApiSettings() throws Exception {
        int databaseSizeBeforeUpdate = apiSettingsRepository.findAll().size();
        apiSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apiSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApiSettings() throws Exception {
        int databaseSizeBeforeUpdate = apiSettingsRepository.findAll().size();
        apiSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apiSettings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiSettings in the database
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApiSettings() throws Exception {
        // Initialize the database
        apiSettingsRepository.saveAndFlush(apiSettings);

        int databaseSizeBeforeDelete = apiSettingsRepository.findAll().size();

        // Delete the apiSettings
        restApiSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, apiSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApiSettings> apiSettingsList = apiSettingsRepository.findAll();
        assertThat(apiSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
