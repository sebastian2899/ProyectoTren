package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tiket;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tiket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TiketRepository extends JpaRepository<Tiket, Long> {}
