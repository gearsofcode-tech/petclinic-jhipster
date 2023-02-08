package tech.gearsofcode.petclinic.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.gearsofcode.petclinic.domain.Vet;

/**
 * Service Interface for managing {@link Vet}.
 */
public interface VetService {
    /**
     * Save a vet.
     *
     * @param vet the entity to save.
     * @return the persisted entity.
     */
    Vet save(Vet vet);

    /**
     * Updates a vet.
     *
     * @param vet the entity to update.
     * @return the persisted entity.
     */
    Vet update(Vet vet);

    /**
     * Partially updates a vet.
     *
     * @param vet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Vet> partialUpdate(Vet vet);

    /**
     * Get all the vets.
     *
     * @return the list of entities.
     */
    List<Vet> findAll();

    /**
     * Get all the vets with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Vet> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" vet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vet> findOne(Long id);

    /**
     * Delete the "id" vet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
