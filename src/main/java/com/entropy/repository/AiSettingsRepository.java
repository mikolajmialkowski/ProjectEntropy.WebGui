package com.entropy.repository;

import com.entropy.domain.AiSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AiSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AiSettingsRepository extends JpaRepository<AiSettings, Long> {}
