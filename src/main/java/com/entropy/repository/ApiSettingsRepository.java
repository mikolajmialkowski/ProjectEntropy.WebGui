package com.entropy.repository;

import com.entropy.domain.ApiSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApiSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApiSettingsRepository extends JpaRepository<ApiSettings, Long> {}
