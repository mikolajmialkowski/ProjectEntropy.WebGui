package com.entropy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.entropy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AiSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AiSettings.class);
        AiSettings aiSettings1 = new AiSettings();
        aiSettings1.setId(1L);
        AiSettings aiSettings2 = new AiSettings();
        aiSettings2.setId(aiSettings1.getId());
        assertThat(aiSettings1).isEqualTo(aiSettings2);
        aiSettings2.setId(2L);
        assertThat(aiSettings1).isNotEqualTo(aiSettings2);
        aiSettings1.setId(null);
        assertThat(aiSettings1).isNotEqualTo(aiSettings2);
    }
}
