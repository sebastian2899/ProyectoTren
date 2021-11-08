package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TrenDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Tren}.
 */
public interface TrenService {
    /**
     * Save a tren.
     *
     * @param trenDTO the entity to save.
     * @return the persisted entity.
     */
    TrenDTO save(TrenDTO trenDTO);

    /**
     * Partially updates a tren.
     *
     * @param trenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrenDTO> partialUpdate(TrenDTO trenDTO);

    /**
     * Get all the trens.
     *
     * @return the list of entities.
     */
    List<TrenDTO> findAll();

    /**
     * Get the "id" tren.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrenDTO> findOne(Long id);

    /**
     * Delete the "id" tren.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
