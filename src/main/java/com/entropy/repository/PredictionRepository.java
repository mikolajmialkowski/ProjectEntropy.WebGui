package com.entropy.repository;

import com.entropy.domain.Prediction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prediction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {}
