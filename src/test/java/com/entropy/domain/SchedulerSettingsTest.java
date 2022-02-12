package com.entropy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.entropy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulerSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulerSettings.class);
        SchedulerSettings schedulerSettings1 = new SchedulerSettings();
        schedulerSettings1.setId(1L);
        SchedulerSettings schedulerSettings2 = new SchedulerSettings();
        schedulerSettings2.setId(schedulerSettings1.getId());
        assertThat(schedulerSettings1).isEqualTo(schedulerSettings2);
        schedulerSettings2.setId(2L);
        assertThat(schedulerSettings1).isNotEqualTo(schedulerSettings2);
        schedulerSettings1.setId(null);
        assertThat(schedulerSettings1).isNotEqualTo(schedulerSettings2);
    }
}
