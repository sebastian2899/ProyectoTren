package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TiketRepository;
import com.mycompany.myapp.service.TiketService;
import com.mycompany.myapp.service.dto.RegistroHistoricoTiketDTO;
import com.mycompany.myapp.service.dto.TiketDTO;
import com.mycompany.myapp.service.dto.TrenDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Tiket}.
 */
@RestController
@RequestMapping("/api")
public class TiketResource {

    private final Logger log = LoggerFactory.getLogger(TiketResource.class);

    private static final String ENTITY_NAME = "tiket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TiketService tiketService;

    private final TiketRepository tiketRepository;

    public TiketResource(TiketService tiketService, TiketRepository tiketRepository) {
        this.tiketService = tiketService;
        this.tiketRepository = tiketRepository;
    }

    /**
     * {@code POST  /tikets} : Create a new tiket.
     *
     * @param tiketDTO the tiketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tiketDTO, or with status {@code 400 (Bad Request)} if the tiket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tikets")
    public ResponseEntity<TiketDTO> createTiket(@RequestBody TiketDTO tiketDTO) throws URISyntaxException {
        log.debug("REST request to save Tiket : {}", tiketDTO);
        if (tiketDTO.getId() != null) {
            throw new BadRequestAlertException("A new tiket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TiketDTO result = tiketService.save(tiketDTO);
        return ResponseEntity
            .created(new URI("/api/tikets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tikets/:id} : Updates an existing tiket.
     *
     * @param id the id of the tiketDTO to save.
     * @param tiketDTO the tiketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tiketDTO,
     * or with status {@code 400 (Bad Request)} if the tiketDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tiketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tikets/{id}")
    public ResponseEntity<TiketDTO> updateTiket(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TiketDTO tiketDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tiket : {}, {}", id, tiketDTO);
        if (tiketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tiketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tiketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TiketDTO result = tiketService.save(tiketDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tiketDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tikets/:id} : Partial updates given fields of an existing tiket, field will ignore if it is null
     *
     * @param id the id of the tiketDTO to save.
     * @param tiketDTO the tiketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tiketDTO,
     * or with status {@code 400 (Bad Request)} if the tiketDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tiketDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tiketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tikets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TiketDTO> partialUpdateTiket(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TiketDTO tiketDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tiket partially : {}, {}", id, tiketDTO);
        if (tiketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tiketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tiketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TiketDTO> result = tiketService.partialUpdate(tiketDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tiketDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tikets} : get all the tikets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tikets in body.
     */
    @GetMapping("/tikets")
    public List<TiketDTO> getTikets() {
        log.debug("REST request to get all Tikets");
        return tiketService.findAll();
    }

    @GetMapping("/Alltikets")
    public List<TiketDTO> getAllTikets() {
        log.debug("REST request to get all Tikets");
        return tiketService.listaAllTikets();
    }

    /* @GetMapping("/puesto/{id}")
  public TrenDTO getPuestos(@PathVariable("idTren") Long idTren) {
        log.debug("REST request to get all Tikets");
        
        return tiketService.totalPuestos(idTren);
    }
    */

    @GetMapping("/puesto/{id}")
    public ResponseEntity<TrenDTO> getPuesto(@PathVariable Long id) {
        log.debug("REST request to get tren : {}", id);
        TrenDTO trenDTO = tiketService.totalPuestos(id);
        return ResponseEntity.ok().body(trenDTO);
    }

    /**
     * {@code GET  /tikets/:id} : get the "id" tiket.
     *
     * @param id the id of the tiketDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tiketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tikets/{id}")
    public ResponseEntity<TiketDTO> getTiket(@PathVariable Long id) {
        log.debug("REST request to get Tiket : {}", id);
        TiketDTO tiketDTO = tiketService.findOne(id);
        return ResponseEntity.ok().body(tiketDTO);
    }

    @GetMapping("/consultarTiketFechas/{fechaInicio}/{fechaFin}")
    public ResponseEntity<List<RegistroHistoricoTiketDTO>> tiketsPorfecha(@PathVariable String fechaInicio, @PathVariable String fechaFin) {
        log.debug("REST request to get all Tikets");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<RegistroHistoricoTiketDTO> rhfp = null;
        try {
            Instant fechaI = format.parse(fechaInicio.substring(0, fechaInicio.indexOf("T"))).toInstant();
            Instant fechaF = format.parse(fechaFin.substring(0, fechaFin.indexOf("T"))).toInstant();
            rhfp = tiketService.consultarTiketFecha(fechaI, fechaF);
        } catch (Exception e) {
            rhfp = null;
        }

        return ResponseEntity.ok().body(rhfp);
    }

    /**
     * {@code DELETE  /tikets/:id} : delete the "id" tiket.
     *
     * @param id the id of the tiketDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tikets/{id}")
    public ResponseEntity<Void> deleteTiket(@PathVariable Long id) {
        log.debug("REST request to delete Tiket : {}", id);
        tiketService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
