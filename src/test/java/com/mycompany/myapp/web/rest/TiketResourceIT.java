package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Tiket;
import com.mycompany.myapp.repository.TiketRepository;
import com.mycompany.myapp.service.dto.TiketDTO;
import com.mycompany.myapp.service.mapper.TiketMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TiketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TiketResourceIT {

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_TREN_ID = 1L;
    private static final Long UPDATED_TREN_ID = 2L;

    private static final Long DEFAULT_CLIENTE_ID = 1L;
    private static final Long UPDATED_CLIENTE_ID = 2L;

    private static final Long DEFAULT_PUESTO = 1L;
    private static final Long UPDATED_PUESTO = 2L;

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_JORDANA = "AAAAAAAAAA";
    private static final String UPDATED_JORDANA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tikets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TiketRepository tiketRepository;

    @Autowired
    private TiketMapper tiketMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTiketMockMvc;

    private Tiket tiket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tiket createEntity(EntityManager em) {
        Tiket tiket = new Tiket()
            .fecha(DEFAULT_FECHA)
            .trenId(DEFAULT_TREN_ID)
            .clienteId(DEFAULT_CLIENTE_ID)
            .puesto(DEFAULT_PUESTO)
            .estado(DEFAULT_ESTADO)
            .jordana(DEFAULT_JORDANA);
        return tiket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tiket createUpdatedEntity(EntityManager em) {
        Tiket tiket = new Tiket()
            .fecha(UPDATED_FECHA)
            .trenId(UPDATED_TREN_ID)
            .clienteId(UPDATED_CLIENTE_ID)
            .puesto(UPDATED_PUESTO)
            .estado(UPDATED_ESTADO)
            .jordana(UPDATED_JORDANA);
        return tiket;
    }

    @BeforeEach
    public void initTest() {
        tiket = createEntity(em);
    }

    @Test
    @Transactional
    void createTiket() throws Exception {
        int databaseSizeBeforeCreate = tiketRepository.findAll().size();
        // Create the Tiket
        TiketDTO tiketDTO = tiketMapper.toDto(tiket);
        restTiketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiketDTO)))
            .andExpect(status().isCreated());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeCreate + 1);
        Tiket testTiket = tiketList.get(tiketList.size() - 1);
        assertThat(testTiket.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testTiket.getTrenId()).isEqualTo(DEFAULT_TREN_ID);
        assertThat(testTiket.getClienteId()).isEqualTo(DEFAULT_CLIENTE_ID);
        assertThat(testTiket.getPuesto()).isEqualTo(DEFAULT_PUESTO);
        assertThat(testTiket.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testTiket.getJordana()).isEqualTo(DEFAULT_JORDANA);
    }

    @Test
    @Transactional
    void createTiketWithExistingId() throws Exception {
        // Create the Tiket with an existing ID
        tiket.setId(1L);
        TiketDTO tiketDTO = tiketMapper.toDto(tiket);

        int databaseSizeBeforeCreate = tiketRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTiketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTikets() throws Exception {
        // Initialize the database
        tiketRepository.saveAndFlush(tiket);

        // Get all the tiketList
        restTiketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tiket.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].trenId").value(hasItem(DEFAULT_TREN_ID.intValue())))
            .andExpect(jsonPath("$.[*].clienteId").value(hasItem(DEFAULT_CLIENTE_ID.intValue())))
            .andExpect(jsonPath("$.[*].puesto").value(hasItem(DEFAULT_PUESTO.intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].jordana").value(hasItem(DEFAULT_JORDANA)));
    }

    @Test
    @Transactional
    void getTiket() throws Exception {
        // Initialize the database
        tiketRepository.saveAndFlush(tiket);

        // Get the tiket
        restTiketMockMvc
            .perform(get(ENTITY_API_URL_ID, tiket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tiket.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.trenId").value(DEFAULT_TREN_ID.intValue()))
            .andExpect(jsonPath("$.clienteId").value(DEFAULT_CLIENTE_ID.intValue()))
            .andExpect(jsonPath("$.puesto").value(DEFAULT_PUESTO.intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.jordana").value(DEFAULT_JORDANA));
    }

    @Test
    @Transactional
    void getNonExistingTiket() throws Exception {
        // Get the tiket
        restTiketMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTiket() throws Exception {
        // Initialize the database
        tiketRepository.saveAndFlush(tiket);

        int databaseSizeBeforeUpdate = tiketRepository.findAll().size();

        // Update the tiket
        Tiket updatedTiket = tiketRepository.findById(tiket.getId()).get();
        // Disconnect from session so that the updates on updatedTiket are not directly saved in db
        em.detach(updatedTiket);
        updatedTiket
            .fecha(UPDATED_FECHA)
            .trenId(UPDATED_TREN_ID)
            .clienteId(UPDATED_CLIENTE_ID)
            .puesto(UPDATED_PUESTO)
            .estado(UPDATED_ESTADO)
            .jordana(UPDATED_JORDANA);
        TiketDTO tiketDTO = tiketMapper.toDto(updatedTiket);

        restTiketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tiketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tiketDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeUpdate);
        Tiket testTiket = tiketList.get(tiketList.size() - 1);
        assertThat(testTiket.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTiket.getTrenId()).isEqualTo(UPDATED_TREN_ID);
        assertThat(testTiket.getClienteId()).isEqualTo(UPDATED_CLIENTE_ID);
        assertThat(testTiket.getPuesto()).isEqualTo(UPDATED_PUESTO);
        assertThat(testTiket.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testTiket.getJordana()).isEqualTo(UPDATED_JORDANA);
    }

    @Test
    @Transactional
    void putNonExistingTiket() throws Exception {
        int databaseSizeBeforeUpdate = tiketRepository.findAll().size();
        tiket.setId(count.incrementAndGet());

        // Create the Tiket
        TiketDTO tiketDTO = tiketMapper.toDto(tiket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTiketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tiketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tiketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTiket() throws Exception {
        int databaseSizeBeforeUpdate = tiketRepository.findAll().size();
        tiket.setId(count.incrementAndGet());

        // Create the Tiket
        TiketDTO tiketDTO = tiketMapper.toDto(tiket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tiketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTiket() throws Exception {
        int databaseSizeBeforeUpdate = tiketRepository.findAll().size();
        tiket.setId(count.incrementAndGet());

        // Create the Tiket
        TiketDTO tiketDTO = tiketMapper.toDto(tiket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiketMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiketDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTiketWithPatch() throws Exception {
        // Initialize the database
        tiketRepository.saveAndFlush(tiket);

        int databaseSizeBeforeUpdate = tiketRepository.findAll().size();

        // Update the tiket using partial update
        Tiket partialUpdatedTiket = new Tiket();
        partialUpdatedTiket.setId(tiket.getId());

        partialUpdatedTiket.fecha(UPDATED_FECHA).trenId(UPDATED_TREN_ID).clienteId(UPDATED_CLIENTE_ID);

        restTiketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTiket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTiket))
            )
            .andExpect(status().isOk());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeUpdate);
        Tiket testTiket = tiketList.get(tiketList.size() - 1);
        assertThat(testTiket.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTiket.getTrenId()).isEqualTo(UPDATED_TREN_ID);
        assertThat(testTiket.getClienteId()).isEqualTo(UPDATED_CLIENTE_ID);
        assertThat(testTiket.getPuesto()).isEqualTo(DEFAULT_PUESTO);
        assertThat(testTiket.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testTiket.getJordana()).isEqualTo(DEFAULT_JORDANA);
    }

    @Test
    @Transactional
    void fullUpdateTiketWithPatch() throws Exception {
        // Initialize the database
        tiketRepository.saveAndFlush(tiket);

        int databaseSizeBeforeUpdate = tiketRepository.findAll().size();

        // Update the tiket using partial update
        Tiket partialUpdatedTiket = new Tiket();
        partialUpdatedTiket.setId(tiket.getId());

        partialUpdatedTiket
            .fecha(UPDATED_FECHA)
            .trenId(UPDATED_TREN_ID)
            .clienteId(UPDATED_CLIENTE_ID)
            .puesto(UPDATED_PUESTO)
            .estado(UPDATED_ESTADO)
            .jordana(UPDATED_JORDANA);

        restTiketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTiket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTiket))
            )
            .andExpect(status().isOk());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeUpdate);
        Tiket testTiket = tiketList.get(tiketList.size() - 1);
        assertThat(testTiket.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTiket.getTrenId()).isEqualTo(UPDATED_TREN_ID);
        assertThat(testTiket.getClienteId()).isEqualTo(UPDATED_CLIENTE_ID);
        assertThat(testTiket.getPuesto()).isEqualTo(UPDATED_PUESTO);
        assertThat(testTiket.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testTiket.getJordana()).isEqualTo(UPDATED_JORDANA);
    }

    @Test
    @Transactional
    void patchNonExistingTiket() throws Exception {
        int databaseSizeBeforeUpdate = tiketRepository.findAll().size();
        tiket.setId(count.incrementAndGet());

        // Create the Tiket
        TiketDTO tiketDTO = tiketMapper.toDto(tiket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTiketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tiketDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tiketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTiket() throws Exception {
        int databaseSizeBeforeUpdate = tiketRepository.findAll().size();
        tiket.setId(count.incrementAndGet());

        // Create the Tiket
        TiketDTO tiketDTO = tiketMapper.toDto(tiket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tiketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTiket() throws Exception {
        int databaseSizeBeforeUpdate = tiketRepository.findAll().size();
        tiket.setId(count.incrementAndGet());

        // Create the Tiket
        TiketDTO tiketDTO = tiketMapper.toDto(tiket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiketMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tiketDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tiket in the database
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTiket() throws Exception {
        // Initialize the database
        tiketRepository.saveAndFlush(tiket);

        int databaseSizeBeforeDelete = tiketRepository.findAll().size();

        // Delete the tiket
        restTiketMockMvc
            .perform(delete(ENTITY_API_URL_ID, tiket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tiket> tiketList = tiketRepository.findAll();
        assertThat(tiketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
