package com.entropy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.entropy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PredictionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prediction.class);
        Prediction prediction1 = new Prediction();
        prediction1.setId(1L);
        Prediction prediction2 = new Prediction();
        prediction2.setId(prediction1.getId());
        assertThat(prediction1).isEqualTo(prediction2);
        prediction2.setId(2L);
        assertThat(prediction1).isNotEqualTo(prediction2);
        prediction1.setId(null);
        assertThat(prediction1).isNotEqualTo(prediction2);
    }
}
