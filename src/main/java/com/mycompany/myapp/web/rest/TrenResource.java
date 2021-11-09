package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TrenRepository;
import com.mycompany.myapp.service.TrenService;
import com.mycompany.myapp.service.dto.TrenDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Tren}.
 */
@RestController
@RequestMapping("/api")
public class TrenResource {

    private final Logger log = LoggerFactory.getLogger(TrenResource.class);

    private static final String ENTITY_NAME = "tren";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrenService trenService;

    private final TrenRepository trenRepository;

    public TrenResource(TrenService trenService, TrenRepository trenRepository) {
        this.trenService = trenService;
        this.trenRepository = trenRepository;
    }

    /**
     * {@code POST  /trens} : Create a new tren.
     *
     * @param trenDTO the trenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trenDTO, or with status {@code 400 (Bad Request)} if the tren has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trens")
    public ResponseEntity<TrenDTO> createTren(@RequestBody TrenDTO trenDTO) throws URISyntaxException {
        log.debug("REST request to save Tren : {}", trenDTO);
        if (trenDTO.getId() != null) {
            throw new BadRequestAlertException("A new tren cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrenDTO result = trenService.save(trenDTO);
        return ResponseEntity
            .created(new URI("/api/trens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trens/:id} : Updates an existing tren.
     *
     * @param id the id of the trenDTO to save.
     * @param trenDTO the trenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trenDTO,
     * or with status {@code 400 (Bad Request)} if the trenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trens/{id}")
    public ResponseEntity<TrenDTO> updateTren(@PathVariable(value = "id", required = false) final Long id, @RequestBody TrenDTO trenDTO)
        throws URISyntaxException {
        log.debug("REST request to update Tren : {}, {}", id, trenDTO);
        if (trenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrenDTO result = trenService.save(trenDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trens/:id} : Partial updates given fields of an existing tren, field will ignore if it is null
     *
     * @param id the id of the trenDTO to save.
     * @param trenDTO the trenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trenDTO,
     * or with status {@code 400 (Bad Request)} if the trenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrenDTO> partialUpdateTren(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrenDTO trenDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tren partially : {}, {}", id, trenDTO);
        if (trenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrenDTO> result = trenService.partialUpdate(trenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trens} : get all the trens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trens in body.
     */
    @GetMapping("/trens")
    public List<TrenDTO> getAllTrens() {
        log.debug("REST request to get all Trens");
        return trenService.findAll();
    }

    /**
     * {@code GET  /trens/:id} : get the "id" tren.
     *
     * @param id the id of the trenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trens/{id}")
    public ResponseEntity<TrenDTO> getTren(@PathVariable Long id) {
        log.debug("REST request to get Tren : {}", id);
        Optional<TrenDTO> trenDTO = trenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trenDTO);
    }

    /**
     * {@code DELETE  /trens/:id} : delete the "id" tren.
     *
     * @param id the id of the trenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trens/{id}")
    public ResponseEntity<Void> deleteTren(@PathVariable Long id) {
        log.debug("REST request to delete Tren : {}", id);
        trenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
