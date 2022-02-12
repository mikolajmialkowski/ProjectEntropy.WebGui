package com.entropy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.entropy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeneralInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeneralInfo.class);
        GeneralInfo generalInfo1 = new GeneralInfo();
        generalInfo1.setId(1L);
        GeneralInfo generalInfo2 = new GeneralInfo();
        generalInfo2.setId(generalInfo1.getId());
        assertThat(generalInfo1).isEqualTo(generalInfo2);
        generalInfo2.setId(2L);
        assertThat(generalInfo1).isNotEqualTo(generalInfo2);
        generalInfo1.setId(null);
        assertThat(generalInfo1).isNotEqualTo(generalInfo2);
    }
}
