package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ConductorRepository;
import com.mycompany.myapp.service.ConductorService;
import com.mycompany.myapp.service.dto.ConductorDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class ConductorResource {

    private final Logger log = LoggerFactory.getLogger(ConductorResource.class);

    private static final String ENTITY_NAME = "conductor";

    @Value("Conductor")
    private String applicationName;

    private final ConductorRepository conductorRepository;

    private final ConductorService conductorService;

    public ConductorResource(ConductorRepository conductorRepository, ConductorService conductorService) {
        this.conductorRepository = conductorRepository;
        this.conductorService = conductorService;
    }

    @PostMapping("/conductores")
    public ResponseEntity<ConductorDTO> createConductor(@RequestBody ConductorDTO conductorDTO) throws URISyntaxException {
        log.debug("REST request to save Conductor : {}", conductorDTO);
        if (conductorDTO.getId() != null) {
            throw new BadRequestAlertException("A new conductor cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ConductorDTO conductDto = conductorService.save(conductorDTO);
        return ResponseEntity
            .created(new URI("/api/conductores" + conductDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, conductDto.getId().toString()))
            .body(conductDto);
    }

    @PutMapping("/conductores/{id}")
    public ResponseEntity<ConductorDTO> updateConductor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConductorDTO conductorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Conductor : {}, {}", id, conductorDTO);
        if (conductorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (!Objects.equals(id, conductorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conductorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConductorDTO conductorDto = conductorService.actualizarConductor(conductorDTO);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conductorDTO.getId().toString()))
            .body(conductorDto);
    }

    @GetMapping("/conductores")
    public List<ConductorDTO> getAll() {
        log.debug("REST request to get all Conductores");
        return conductorService.findAll();
    }

    @GetMapping("/filtroConductor/{nombreApellido}")
    public ResponseEntity<List<ConductorDTO>> FiltroConductor(@PathVariable("nombreApellido") String nombreApellido) {
        log.debug("REST request to get all nombreApellido");

        List<ConductorDTO> conductores = conductorService.ValidarFiltro(nombreApellido);

        return ResponseEntity.ok().body(conductores);
    }

    @GetMapping("/consultarFotoConductor/{idConductor}")
    public ResponseEntity<byte[]> ConsultarFotoConductor(@PathVariable("idConductor") Long idConductor) {
        log.debug("REST request to get all nombreApellido");

        byte[] bytes = conductorService.consultarFoto(idConductor);

        return ResponseEntity.ok().body(bytes);
    }

    @PostMapping(path = "/cargarFoto", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Long> cargarFotoConductor(@RequestParam("file") MultipartFile multipartFile) throws URISyntaxException {
        log.debug("REST request to get all nombreApellido");

        Long respuesta = null;

        try {
            respuesta = conductorService.cargarFotoConductor(multipartFile.getBytes());
        } catch (IOException e) {
            return ResponseEntity.created(new URI("/api/cargarFoto/" + respuesta)).body(respuesta);
        }
        return ResponseEntity.created(new URI("/api/cargarFoto/" + respuesta)).body(respuesta);
    }

    @PostMapping(path = "/actualizarFotoConductor/{idConductor}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<byte[]> actualizarFotoConductor(
        @PathVariable Long idConductor,
        @RequestParam("file") MultipartFile multipartFile
    ) throws URISyntaxException {
        log.debug("REST request to udate foto");
        byte[] respuesta = null;
        try {
            respuesta = conductorService.actualizarFotoConductor(multipartFile.getBytes(), idConductor);
        } catch (IOException e) {
            return ResponseEntity.created(new URI("/api/actualizarFotoConductor/" + respuesta)).body(respuesta);
        }
        return ResponseEntity.ok().body(respuesta);
    }

    @GetMapping("/conductores/{id}")
    public ResponseEntity<ConductorDTO> getConductor(@PathVariable("id") Long id) {
        log.debug("REST request to get conductor {}" + id);

        Optional<ConductorDTO> conductorDTO = conductorService.findOne(id);

        return ResponseUtil.wrapOrNotFound(conductorDTO);
    }

    @DeleteMapping("/conductores/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        log.debug("REST request to delete condcutor {}" + id);
        conductorService.delete(id);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
