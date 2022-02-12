package com.entropy.web.rest;

import static com.entropy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.entropy.IntegrationTest;
import com.entropy.domain.GeneralInfo;
import com.entropy.repository.GeneralInfoRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link GeneralInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeneralInfoResourceIT {

    private static final BigDecimal DEFAULT_RECORDS_IN_DATA_BASE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_RECORDS_IN_DATA_BASE_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_API_CALLS_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_API_CALLS_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_API_STATISTICS = "AAAAAAAAAA";
    private static final String UPDATED_API_STATISTICS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/general-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GeneralInfoRepository generalInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeneralInfoMockMvc;

    private GeneralInfo generalInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeneralInfo createEntity(EntityManager em) {
        GeneralInfo generalInfo = new GeneralInfo()
            .recordsInDataBaseAmount(DEFAULT_RECORDS_IN_DATA_BASE_AMOUNT)
            .apiCallsAmount(DEFAULT_API_CALLS_AMOUNT)
            .apiStatistics(DEFAULT_API_STATISTICS);
        return generalInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeneralInfo createUpdatedEntity(EntityManager em) {
        GeneralInfo generalInfo = new GeneralInfo()
            .recordsInDataBaseAmount(UPDATED_RECORDS_IN_DATA_BASE_AMOUNT)
            .apiCallsAmount(UPDATED_API_CALLS_AMOUNT)
            .apiStatistics(UPDATED_API_STATISTICS);
        return generalInfo;
    }

    @BeforeEach
    public void initTest() {
        generalInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createGeneralInfo() throws Exception {
        int databaseSizeBeforeCreate = generalInfoRepository.findAll().size();
        // Create the GeneralInfo
        restGeneralInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generalInfo)))
            .andExpect(status().isCreated());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeCreate + 1);
        GeneralInfo testGeneralInfo = generalInfoList.get(generalInfoList.size() - 1);
        assertThat(testGeneralInfo.getRecordsInDataBaseAmount()).isEqualByComparingTo(DEFAULT_RECORDS_IN_DATA_BASE_AMOUNT);
        assertThat(testGeneralInfo.getApiCallsAmount()).isEqualByComparingTo(DEFAULT_API_CALLS_AMOUNT);
        assertThat(testGeneralInfo.getApiStatistics()).isEqualTo(DEFAULT_API_STATISTICS);
    }

    @Test
    @Transactional
    void createGeneralInfoWithExistingId() throws Exception {
        // Create the GeneralInfo with an existing ID
        generalInfo.setId(1L);

        int databaseSizeBeforeCreate = generalInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneralInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generalInfo)))
            .andExpect(status().isBadRequest());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGeneralInfos() throws Exception {
        // Initialize the database
        generalInfoRepository.saveAndFlush(generalInfo);

        // Get all the generalInfoList
        restGeneralInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generalInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordsInDataBaseAmount").value(hasItem(sameNumber(DEFAULT_RECORDS_IN_DATA_BASE_AMOUNT))))
            .andExpect(jsonPath("$.[*].apiCallsAmount").value(hasItem(sameNumber(DEFAULT_API_CALLS_AMOUNT))))
            .andExpect(jsonPath("$.[*].apiStatistics").value(hasItem(DEFAULT_API_STATISTICS)));
    }

    @Test
    @Transactional
    void getGeneralInfo() throws Exception {
        // Initialize the database
        generalInfoRepository.saveAndFlush(generalInfo);

        // Get the generalInfo
        restGeneralInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, generalInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(generalInfo.getId().intValue()))
            .andExpect(jsonPath("$.recordsInDataBaseAmount").value(sameNumber(DEFAULT_RECORDS_IN_DATA_BASE_AMOUNT)))
            .andExpect(jsonPath("$.apiCallsAmount").value(sameNumber(DEFAULT_API_CALLS_AMOUNT)))
            .andExpect(jsonPath("$.apiStatistics").value(DEFAULT_API_STATISTICS));
    }

    @Test
    @Transactional
    void getNonExistingGeneralInfo() throws Exception {
        // Get the generalInfo
        restGeneralInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGeneralInfo() throws Exception {
        // Initialize the database
        generalInfoRepository.saveAndFlush(generalInfo);

        int databaseSizeBeforeUpdate = generalInfoRepository.findAll().size();

        // Update the generalInfo
        GeneralInfo updatedGeneralInfo = generalInfoRepository.findById(generalInfo.getId()).get();
        // Disconnect from session so that the updates on updatedGeneralInfo are not directly saved in db
        em.detach(updatedGeneralInfo);
        updatedGeneralInfo
            .recordsInDataBaseAmount(UPDATED_RECORDS_IN_DATA_BASE_AMOUNT)
            .apiCallsAmount(UPDATED_API_CALLS_AMOUNT)
            .apiStatistics(UPDATED_API_STATISTICS);

        restGeneralInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGeneralInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGeneralInfo))
            )
            .andExpect(status().isOk());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeUpdate);
        GeneralInfo testGeneralInfo = generalInfoList.get(generalInfoList.size() - 1);
        assertThat(testGeneralInfo.getRecordsInDataBaseAmount()).isEqualByComparingTo(UPDATED_RECORDS_IN_DATA_BASE_AMOUNT);
        assertThat(testGeneralInfo.getApiCallsAmount()).isEqualByComparingTo(UPDATED_API_CALLS_AMOUNT);
        assertThat(testGeneralInfo.getApiStatistics()).isEqualTo(UPDATED_API_STATISTICS);
    }

    @Test
    @Transactional
    void putNonExistingGeneralInfo() throws Exception {
        int databaseSizeBeforeUpdate = generalInfoRepository.findAll().size();
        generalInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneralInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generalInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generalInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeneralInfo() throws Exception {
        int databaseSizeBeforeUpdate = generalInfoRepository.findAll().size();
        generalInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generalInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeneralInfo() throws Exception {
        int databaseSizeBeforeUpdate = generalInfoRepository.findAll().size();
        generalInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generalInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeneralInfoWithPatch() throws Exception {
        // Initialize the database
        generalInfoRepository.saveAndFlush(generalInfo);

        int databaseSizeBeforeUpdate = generalInfoRepository.findAll().size();

        // Update the generalInfo using partial update
        GeneralInfo partialUpdatedGeneralInfo = new GeneralInfo();
        partialUpdatedGeneralInfo.setId(generalInfo.getId());

        partialUpdatedGeneralInfo.recordsInDataBaseAmount(UPDATED_RECORDS_IN_DATA_BASE_AMOUNT).apiCallsAmount(UPDATED_API_CALLS_AMOUNT);

        restGeneralInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneralInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneralInfo))
            )
            .andExpect(status().isOk());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeUpdate);
        GeneralInfo testGeneralInfo = generalInfoList.get(generalInfoList.size() - 1);
        assertThat(testGeneralInfo.getRecordsInDataBaseAmount()).isEqualByComparingTo(UPDATED_RECORDS_IN_DATA_BASE_AMOUNT);
        assertThat(testGeneralInfo.getApiCallsAmount()).isEqualByComparingTo(UPDATED_API_CALLS_AMOUNT);
        assertThat(testGeneralInfo.getApiStatistics()).isEqualTo(DEFAULT_API_STATISTICS);
    }

    @Test
    @Transactional
    void fullUpdateGeneralInfoWithPatch() throws Exception {
        // Initialize the database
        generalInfoRepository.saveAndFlush(generalInfo);

        int databaseSizeBeforeUpdate = generalInfoRepository.findAll().size();

        // Update the generalInfo using partial update
        GeneralInfo partialUpdatedGeneralInfo = new GeneralInfo();
        partialUpdatedGeneralInfo.setId(generalInfo.getId());

        partialUpdatedGeneralInfo
            .recordsInDataBaseAmount(UPDATED_RECORDS_IN_DATA_BASE_AMOUNT)
            .apiCallsAmount(UPDATED_API_CALLS_AMOUNT)
            .apiStatistics(UPDATED_API_STATISTICS);

        restGeneralInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneralInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneralInfo))
            )
            .andExpect(status().isOk());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeUpdate);
        GeneralInfo testGeneralInfo = generalInfoList.get(generalInfoList.size() - 1);
        assertThat(testGeneralInfo.getRecordsInDataBaseAmount()).isEqualByComparingTo(UPDATED_RECORDS_IN_DATA_BASE_AMOUNT);
        assertThat(testGeneralInfo.getApiCallsAmount()).isEqualByComparingTo(UPDATED_API_CALLS_AMOUNT);
        assertThat(testGeneralInfo.getApiStatistics()).isEqualTo(UPDATED_API_STATISTICS);
    }

    @Test
    @Transactional
    void patchNonExistingGeneralInfo() throws Exception {
        int databaseSizeBeforeUpdate = generalInfoRepository.findAll().size();
        generalInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneralInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, generalInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generalInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeneralInfo() throws Exception {
        int databaseSizeBeforeUpdate = generalInfoRepository.findAll().size();
        generalInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generalInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeneralInfo() throws Exception {
        int databaseSizeBeforeUpdate = generalInfoRepository.findAll().size();
        generalInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(generalInfo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeneralInfo in the database
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeneralInfo() throws Exception {
        // Initialize the database
        generalInfoRepository.saveAndFlush(generalInfo);

        int databaseSizeBeforeDelete = generalInfoRepository.findAll().size();

        // Delete the generalInfo
        restGeneralInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, generalInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GeneralInfo> generalInfoList = generalInfoRepository.findAll();
        assertThat(generalInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
