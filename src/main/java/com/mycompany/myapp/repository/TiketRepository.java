package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cliente;
import com.mycompany.myapp.domain.Tiket;
import com.mycompany.myapp.service.dto.TiketDTO;
import java.time.Instant;
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
    @Query("SELECT t FROM Tiket t WHERE t.id IN (SELECT ti.id FROM Tiket ti)")
    List<Tiket> consultaClientes();

    @Query("SELECT c.nombre FROM Cliente c WHERE c.id=:idPersona")
    String nombreCliente(@Param("idPersona") Long idPersona);

    @Query("SELECT c FROM Cliente c WHERE c.id= :id")
    Cliente buscarCliente(@Param("id") Long id);

    @Query("SELECT t FROM Tiket t WHERE t.id= :id")
    Tiket findOne(@Param("id") Long id);

    @Query("SELECT t FROM Tiket t WHERE t.estado = 'Activo'")
    List<Tiket> tiketsActivos();

    @Query("SELECT t FROM Tiket t WHERE t.estado = 'EN ESPERA' OR t.estado = 'NO DISPONIBLE'")
    List<Tiket> tiketsProcesos();

    @Query("SELECT t FROM Tiket t WHERE t.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Tiket> consultarTiketPorfecha(@Param("fechaInicio") Instant fechaInicio, @Param("fechaFin") Instant fechaFin);
}
