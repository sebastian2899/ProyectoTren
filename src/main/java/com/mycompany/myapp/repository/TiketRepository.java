package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tiket;
import com.mycompany.myapp.domain.Tren;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tiket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TiketRepository extends JpaRepository<Tiket, Long> {
    @Query("SELECT t FROM Tiket t WHERE t.id IN (SELECT MAX(ti.id) FROM Tiket ti GROUP BY ti.clienteId)")
    List<Tiket> consultaClientes();

    @Query("SELECT c.nombre FROM Cliente c WHERE c.id=:idPersona")
    String nombreCliente(@Param("idPersona") Long idPersona);
}
