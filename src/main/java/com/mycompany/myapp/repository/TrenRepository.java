package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tren;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tren entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrenRepository extends JpaRepository<Tren, Long> {
    @Query("SELECT t FROM Tren t WHERE t.id=:idTren")
    Tren consultarPuesto(@Param("idTren") Long idTren);
}
