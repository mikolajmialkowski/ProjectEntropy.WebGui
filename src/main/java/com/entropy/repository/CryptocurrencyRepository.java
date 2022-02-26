package com.entropy.repository;

import com.entropy.domain.Cryptocurrency;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cryptocurrency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {}
