package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tren;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tren entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrenRepository extends JpaRepository<Tren, Long> {}
