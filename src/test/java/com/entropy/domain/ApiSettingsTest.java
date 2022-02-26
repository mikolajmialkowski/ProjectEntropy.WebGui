package com.entropy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.entropy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApiSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiSettings.class);
        ApiSettings apiSettings1 = new ApiSettings();
        apiSettings1.setId(1L);
        ApiSettings apiSettings2 = new ApiSettings();
        apiSettings2.setId(apiSettings1.getId());
        assertThat(apiSettings1).isEqualTo(apiSettings2);
        apiSettings2.setId(2L);
        assertThat(apiSettings1).isNotEqualTo(apiSettings2);
        apiSettings1.setId(null);
        assertThat(apiSettings1).isNotEqualTo(apiSettings2);
    }
}
