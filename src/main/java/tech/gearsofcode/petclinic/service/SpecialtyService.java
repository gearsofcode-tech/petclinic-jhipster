package tech.gearsofcode.petclinic.service;

import java.util.List;
import java.util.Optional;
import tech.gearsofcode.petclinic.domain.Specialty;

/**
 * Service Interface for managing {@link Specialty}.
 */
public interface SpecialtyService {
    /**
     * Save a specialty.
     *
     * @param specialty the entity to save.
     * @return the persisted entity.
     */
    Specialty save(Specialty specialty);

    /**
     * Updates a specialty.
     *
     * @param specialty the entity to update.
     * @return the persisted entity.
     */
    Specialty update(Specialty specialty);

    /**
     * Partially updates a specialty.
     *
     * @param specialty the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Specialty> partialUpdate(Specialty specialty);

    /**
     * Get all the specialties.
     *
     * @return the list of entities.
     */
    List<Specialty> findAll();

    /**
     * Get the "id" specialty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Specialty> findOne(Long id);

    /**
     * Delete the "id" specialty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
