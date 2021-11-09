package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TiketDTO;
import com.mycompany.myapp.service.dto.TrenDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Tiket}.
 */
public interface TiketService {
    /**
     * Save a tiket.
     *
     * @param tiketDTO the entity to save.
     * @return the persisted entity.
     */
    TiketDTO save(TiketDTO tiketDTO);

    /**
     * Partially updates a tiket.
     *
     * @param tiketDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TiketDTO> partialUpdate(TiketDTO tiketDTO);

    /**
     * Get all the tikets.
     *
     * @return the list of entities.
     */
    List<TiketDTO> findAll();

    /**
     * Get the "id" tiket.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TiketDTO> findOne(Long id);

    /**
     * Delete the "id" tiket.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    TrenDTO totalPuestos(Long id);
}
