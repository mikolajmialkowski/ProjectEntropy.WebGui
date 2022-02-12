package com.entropy.repository;

import com.entropy.domain.GeneralInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GeneralInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneralInfoRepository extends JpaRepository<GeneralInfo, Long> {}
