package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Tren;
import com.mycompany.myapp.repository.TrenRepository;
import com.mycompany.myapp.service.dto.TrenDTO;
import com.mycompany.myapp.service.mapper.TrenMapper;
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
 * Integration tests for the {@link TrenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrenResourceIT {

    private static final Integer DEFAULT_ASIENTOS = 1;
    private static final Integer UPDATED_ASIENTOS = 2;

    private static final String ENTITY_API_URL = "/api/trens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrenRepository trenRepository;

    @Autowired
    private TrenMapper trenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrenMockMvc;

    private Tren tren;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tren createEntity(EntityManager em) {
        Tren tren = new Tren().asientos(DEFAULT_ASIENTOS);
        return tren;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tren createUpdatedEntity(EntityManager em) {
        Tren tren = new Tren().asientos(UPDATED_ASIENTOS);
        return tren;
    }

    @BeforeEach
    public void initTest() {
        tren = createEntity(em);
    }

    @Test
    @Transactional
    void createTren() throws Exception {
        int databaseSizeBeforeCreate = trenRepository.findAll().size();
        // Create the Tren
        TrenDTO trenDTO = trenMapper.toDto(tren);
        restTrenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trenDTO)))
            .andExpect(status().isCreated());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeCreate + 1);
        Tren testTren = trenList.get(trenList.size() - 1);
        assertThat(testTren.getAsientos()).isEqualTo(DEFAULT_ASIENTOS);
    }

    @Test
    @Transactional
    void createTrenWithExistingId() throws Exception {
        // Create the Tren with an existing ID
        tren.setId(1L);
        TrenDTO trenDTO = trenMapper.toDto(tren);

        int databaseSizeBeforeCreate = trenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrens() throws Exception {
        // Initialize the database
        trenRepository.saveAndFlush(tren);

        // Get all the trenList
        restTrenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tren.getId().intValue())))
            .andExpect(jsonPath("$.[*].asientos").value(hasItem(DEFAULT_ASIENTOS)));
    }

    @Test
    @Transactional
    void getTren() throws Exception {
        // Initialize the database
        trenRepository.saveAndFlush(tren);

        // Get the tren
        restTrenMockMvc
            .perform(get(ENTITY_API_URL_ID, tren.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tren.getId().intValue()))
            .andExpect(jsonPath("$.asientos").value(DEFAULT_ASIENTOS));
    }

    @Test
    @Transactional
    void getNonExistingTren() throws Exception {
        // Get the tren
        restTrenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTren() throws Exception {
        // Initialize the database
        trenRepository.saveAndFlush(tren);

        int databaseSizeBeforeUpdate = trenRepository.findAll().size();

        // Update the tren
        Tren updatedTren = trenRepository.findById(tren.getId()).get();
        // Disconnect from session so that the updates on updatedTren are not directly saved in db
        em.detach(updatedTren);
        updatedTren.asientos(UPDATED_ASIENTOS);
        TrenDTO trenDTO = trenMapper.toDto(updatedTren);

        restTrenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeUpdate);
        Tren testTren = trenList.get(trenList.size() - 1);
        assertThat(testTren.getAsientos()).isEqualTo(UPDATED_ASIENTOS);
    }

    @Test
    @Transactional
    void putNonExistingTren() throws Exception {
        int databaseSizeBeforeUpdate = trenRepository.findAll().size();
        tren.setId(count.incrementAndGet());

        // Create the Tren
        TrenDTO trenDTO = trenMapper.toDto(tren);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTren() throws Exception {
        int databaseSizeBeforeUpdate = trenRepository.findAll().size();
        tren.setId(count.incrementAndGet());

        // Create the Tren
        TrenDTO trenDTO = trenMapper.toDto(tren);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTren() throws Exception {
        int databaseSizeBeforeUpdate = trenRepository.findAll().size();
        tren.setId(count.incrementAndGet());

        // Create the Tren
        TrenDTO trenDTO = trenMapper.toDto(tren);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrenWithPatch() throws Exception {
        // Initialize the database
        trenRepository.saveAndFlush(tren);

        int databaseSizeBeforeUpdate = trenRepository.findAll().size();

        // Update the tren using partial update
        Tren partialUpdatedTren = new Tren();
        partialUpdatedTren.setId(tren.getId());

        partialUpdatedTren.asientos(UPDATED_ASIENTOS);

        restTrenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTren.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTren))
            )
            .andExpect(status().isOk());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeUpdate);
        Tren testTren = trenList.get(trenList.size() - 1);
        assertThat(testTren.getAsientos()).isEqualTo(UPDATED_ASIENTOS);
    }

    @Test
    @Transactional
    void fullUpdateTrenWithPatch() throws Exception {
        // Initialize the database
        trenRepository.saveAndFlush(tren);

        int databaseSizeBeforeUpdate = trenRepository.findAll().size();

        // Update the tren using partial update
        Tren partialUpdatedTren = new Tren();
        partialUpdatedTren.setId(tren.getId());

        partialUpdatedTren.asientos(UPDATED_ASIENTOS);

        restTrenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTren.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTren))
            )
            .andExpect(status().isOk());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeUpdate);
        Tren testTren = trenList.get(trenList.size() - 1);
        assertThat(testTren.getAsientos()).isEqualTo(UPDATED_ASIENTOS);
    }

    @Test
    @Transactional
    void patchNonExistingTren() throws Exception {
        int databaseSizeBeforeUpdate = trenRepository.findAll().size();
        tren.setId(count.incrementAndGet());

        // Create the Tren
        TrenDTO trenDTO = trenMapper.toDto(tren);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTren() throws Exception {
        int databaseSizeBeforeUpdate = trenRepository.findAll().size();
        tren.setId(count.incrementAndGet());

        // Create the Tren
        TrenDTO trenDTO = trenMapper.toDto(tren);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTren() throws Exception {
        int databaseSizeBeforeUpdate = trenRepository.findAll().size();
        tren.setId(count.incrementAndGet());

        // Create the Tren
        TrenDTO trenDTO = trenMapper.toDto(tren);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tren in the database
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTren() throws Exception {
        // Initialize the database
        trenRepository.saveAndFlush(tren);

        int databaseSizeBeforeDelete = trenRepository.findAll().size();

        // Delete the tren
        restTrenMockMvc
            .perform(delete(ENTITY_API_URL_ID, tren.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tren> trenList = trenRepository.findAll();
        assertThat(trenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
