package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ClienteRepository;
import com.mycompany.myapp.service.ClienteService;
import com.mycompany.myapp.service.dto.ClienteDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Cliente}.
 */
@RestController
@RequestMapping("/api")
public class ClienteResource {

    private final Logger log = LoggerFactory.getLogger(ClienteResource.class);

    private static final String ENTITY_NAME = "cliente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClienteService clienteService;

    private final ClienteRepository clienteRepository;

    public ClienteResource(ClienteService clienteService, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    /**
     * {@code POST  /clientes} : Create a new cliente.
     *
     * @param clienteDTO the clienteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clienteDTO, or with status {@code 400 (Bad Request)} if the cliente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clientes")
    public ResponseEntity<ClienteDTO> createCliente(@RequestBody ClienteDTO clienteDTO) throws URISyntaxException {
        log.debug("REST request to save Cliente : {}", clienteDTO);
        if (clienteDTO.getId() != null) {
            throw new BadRequestAlertException("A new cliente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClienteDTO result = clienteService.save(clienteDTO);
        return ResponseEntity
            .created(new URI("/api/clientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clientes/:id} : Updates an existing cliente.
     *
     * @param id the id of the clienteDTO to save.
     * @param clienteDTO the clienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clienteDTO,
     * or with status {@code 400 (Bad Request)} if the clienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clientes/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClienteDTO clienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cliente : {}, {}", id, clienteDTO);
        if (clienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClienteDTO result = clienteService.actualizarCliente(clienteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clienteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clientes/:id} : Partial updates given fields of an existing cliente, field will ignore if it is null
     *
     * @param id the id of the clienteDTO to save.
     * @param clienteDTO the clienteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clienteDTO,
     * or with status {@code 400 (Bad Request)} if the clienteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clienteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clienteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clientes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClienteDTO> partialUpdateCliente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClienteDTO clienteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cliente partially : {}, {}", id, clienteDTO);
        if (clienteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clienteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClienteDTO> result = clienteService.partialUpdate(clienteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clienteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /clientes} : get all the clientes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientes in body.
     */
    @GetMapping("/clientes")
    public List<ClienteDTO> getAllClientes() {
        log.debug("REST request to get all Clientes");
        return clienteService.findAll();
    }

    /**
     * {@code GET  /clientes/:id} : get the "id" cliente.
     *
     * @param id the id of the clienteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clientes/{id}")
    public ResponseEntity<ClienteDTO> getCliente(@PathVariable Long id) {
        log.debug("REST request to get Cliente : {}", id);
        Optional<ClienteDTO> clienteDTO = clienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clienteDTO);
    }

    @PostMapping(path = "/cargarFotoCliente", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Long> cargarFoto(@RequestParam("file") MultipartFile multipartfile) throws URISyntaxException {
        log.debug("REST request to uploadFoto Cliente : {}");

        Long respuesta = null;

        try {
            respuesta = clienteService.cargarFotoCliente(multipartfile.getBytes());
        } catch (IOException e) {
            return ResponseEntity.created(new URI("/api/cargarFotoCliente/" + respuesta)).body(respuesta);
        }

        return ResponseEntity.created(new URI("/api/cargarFotoCliente/" + respuesta)).body(respuesta);
    }

    @PostMapping("/actualizarFotoCliente/{idCliente}")
    public ResponseEntity<byte[]> actualizarFotoClient(@PathVariable Long idCliente, @RequestParam("file") MultipartFile multipartFile)
        throws URISyntaxException {
        log.debug("REST request to updateFile{}" + idCliente);

        byte[] respuesta = null;

        try {
            respuesta = clienteService.actualizarFotoCliente(multipartFile.getBytes(), idCliente);
        } catch (IOException e) {
            return ResponseEntity.created(new URI("/api/actualizarFotoCliente/" + respuesta)).body(respuesta);
        }

        return ResponseEntity.ok().body(respuesta);
    }

    @GetMapping("/filtro/{nombreFiltro}")
    public ResponseEntity<List<ClienteDTO>> clientesFiltrados(@PathVariable String nombreFiltro) {
        log.debug("REST request to get Clientes : {}", nombreFiltro);

        List<ClienteDTO> clientes = clienteService.validarFiltro(nombreFiltro);

        return ResponseEntity.ok().body(clientes);
    }

    @GetMapping("/consultarFotoCliente/{idCliente}")
    public ResponseEntity<byte[]> consultarFotoCliente(@PathVariable Long idCliente) {
        log.debug("REST request to get file : {}", idCliente);

        byte[] bytes = clienteService.consultarFoto(idCliente);

        return ResponseEntity.ok().body(bytes);
    }

    /**
     * {@code DELETE  /clientes/:id} : delete the "id" cliente.
     *
     * @param id the id of the clienteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        log.debug("REST request to delete Cliente : {}", id);
        clienteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
