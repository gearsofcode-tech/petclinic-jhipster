package tech.gearsofcode.petclinic.service;

import java.util.List;
import java.util.Optional;
import tech.gearsofcode.petclinic.domain.PetType;

/**
 * Service Interface for managing {@link PetType}.
 */
public interface PetTypeService {
    /**
     * Save a petType.
     *
     * @param petType the entity to save.
     * @return the persisted entity.
     */
    PetType save(PetType petType);

    /**
     * Updates a petType.
     *
     * @param petType the entity to update.
     * @return the persisted entity.
     */
    PetType update(PetType petType);

    /**
     * Partially updates a petType.
     *
     * @param petType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PetType> partialUpdate(PetType petType);

    /**
     * Get all the petTypes.
     *
     * @return the list of entities.
     */
    List<PetType> findAll();

    /**
     * Get the "id" petType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PetType> findOne(Long id);

    /**
     * Delete the "id" petType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
