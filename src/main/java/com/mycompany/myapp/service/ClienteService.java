package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ClienteDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Cliente}.
 */
public interface ClienteService {
    /**
     * Save a cliente.
     *
     * @param clienteDTO the entity to save.
     * @return the persisted entity.
     */
    ClienteDTO save(ClienteDTO clienteDTO);

    /**
     * Partially updates a cliente.
     *
     * @param clienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClienteDTO> partialUpdate(ClienteDTO clienteDTO);

    /**
     * Get all the clientes.
     *
     * @return the list of entities.
     */
    List<ClienteDTO> findAll();

    /**
     * Get the "id" cliente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClienteDTO> findOne(Long id);

    List<ClienteDTO> buscarConFiltros(String nombreFiltro);

    List<ClienteDTO> validarFiltro(String nombreFiltro);

    Long cargarFotoCliente(byte[] foto);

    byte[] actualizarFotoCliente(byte[] bytes, Long idCliente);

    ClienteDTO actualizarCliente(ClienteDTO cliente);

    byte[] consultarFoto(Long idCliente);

    /**
     * Delete the "id" cliente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
