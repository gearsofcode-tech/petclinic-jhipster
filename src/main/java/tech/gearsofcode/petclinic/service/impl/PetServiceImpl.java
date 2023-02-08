package tech.gearsofcode.petclinic.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.gearsofcode.petclinic.domain.Pet;
import tech.gearsofcode.petclinic.repository.PetRepository;
import tech.gearsofcode.petclinic.service.PetService;

/**
 * Service Implementation for managing {@link Pet}.
 */
@Service
@Transactional
public class PetServiceImpl implements PetService {

    private final Logger log = LoggerFactory.getLogger(PetServiceImpl.class);

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet save(Pet pet) {
        log.debug("Request to save Pet : {}", pet);
        return petRepository.save(pet);
    }

    @Override
    public Pet update(Pet pet) {
        log.debug("Request to update Pet : {}", pet);
        return petRepository.save(pet);
    }

    @Override
    public Optional<Pet> partialUpdate(Pet pet) {
        log.debug("Request to partially update Pet : {}", pet);

        return petRepository
            .findById(pet.getId())
            .map(existingPet -> {
                if (pet.getName() != null) {
                    existingPet.setName(pet.getName());
                }
                if (pet.getBirthdate() != null) {
                    existingPet.setBirthdate(pet.getBirthdate());
                }

                return existingPet;
            })
            .map(petRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findAll() {
        log.debug("Request to get all Pets");
        return petRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pet> findOne(Long id) {
        log.debug("Request to get Pet : {}", id);
        return petRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pet : {}", id);
        petRepository.deleteById(id);
    }
}
