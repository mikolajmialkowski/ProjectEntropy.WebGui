package com.entropy.repository;

import com.entropy.domain.SchedulerSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchedulerSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedulerSettingsRepository extends JpaRepository<SchedulerSettings, Long> {}
