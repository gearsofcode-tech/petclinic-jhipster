package tech.gearsofcode.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import tech.gearsofcode.petclinic.IntegrationTest;
import tech.gearsofcode.petclinic.domain.PetType;
import tech.gearsofcode.petclinic.repository.PetTypeRepository;

/**
 * Integration tests for the {@link PetTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PetTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pet-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPetTypeMockMvc;

    private PetType petType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PetType createEntity(EntityManager em) {
        PetType petType = new PetType().name(DEFAULT_NAME);
        return petType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PetType createUpdatedEntity(EntityManager em) {
        PetType petType = new PetType().name(UPDATED_NAME);
        return petType;
    }

    @BeforeEach
    public void initTest() {
        petType = createEntity(em);
    }

    @Test
    @Transactional
    void createPetType() throws Exception {
        int databaseSizeBeforeCreate = petTypeRepository.findAll().size();
        // Create the PetType
        restPetTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petType))
            )
            .andExpect(status().isCreated());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);
        assertThat(testPetType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPetTypeWithExistingId() throws Exception {
        // Create the PetType with an existing ID
        petType.setId(1L);

        int databaseSizeBeforeCreate = petTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPetTypes() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(petType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPetType() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get the petType
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, petType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(petType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPetType() throws Exception {
        // Get the petType
        restPetTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPetType() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();

        // Update the petType
        PetType updatedPetType = petTypeRepository.findById(petType.getId()).get();
        // Disconnect from session so that the updates on updatedPetType are not directly saved in db
        em.detach(updatedPetType);
        updatedPetType.name(UPDATED_NAME);

        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPetType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPetType))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);
        assertThat(testPetType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePetTypeWithPatch() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();

        // Update the petType using partial update
        PetType partialUpdatedPetType = new PetType();
        partialUpdatedPetType.setId(petType.getId());

        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPetType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPetType))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);
        assertThat(testPetType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePetTypeWithPatch() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();

        // Update the petType using partial update
        PetType partialUpdatedPetType = new PetType();
        partialUpdatedPetType.setId(petType.getId());

        partialUpdatedPetType.name(UPDATED_NAME);

        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPetType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPetType))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);
        assertThat(testPetType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, petType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(petType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(petType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(petType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePetType() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        int databaseSizeBeforeDelete = petTypeRepository.findAll().size();

        // Delete the petType
        restPetTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, petType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
