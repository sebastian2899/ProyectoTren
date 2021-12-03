package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Conductor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConductorRepository extends JpaRepository<Conductor, Long> {
    @Query("SELECT c FROM Conductor c WHERE c.nombres LIKE :nombreApellido OR c.apellidos LIKE :nombreApellido ")
    public List<Conductor> conductorFiltro(@Param("nombreApellido") String nombreApellido);

    @Query("SELECT c FROM Conductor c WHERE c.id= :id")
    public Conductor buscarConductor(@Param("id") Long id);

    @Query("SELECT c.foto FROM Conductor c WHERE c.id=:id")
    public byte[] fotoConductor(Long idConducor);
}
