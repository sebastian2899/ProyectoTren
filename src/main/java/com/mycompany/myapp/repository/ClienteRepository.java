package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cliente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT c FROM Cliente c WHERE c.nombre LIKE :nombreFiltro")
    List<Cliente> clientesPorFiltro(@Param("nombreFiltro") String nombreFiltro);

    @Query("SELECT c FROM Cliente c WHERE c.id= :id")
    Cliente buscarCliente(@Param("id") Long id);

    @Query("SELECT c.foto FROM Cliente c WHERE c.id =:idCliente")
    byte[] consultarFotoCliente(@Param("idCliente") Long idCliente);
}
