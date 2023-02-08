package tech.gearsofcode.petclinic.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.gearsofcode.petclinic.domain.PetType;
import tech.gearsofcode.petclinic.repository.PetTypeRepository;
import tech.gearsofcode.petclinic.service.PetTypeService;

/**
 * Service Implementation for managing {@link PetType}.
 */
@Service
@Transactional
public class PetTypeServiceImpl implements PetTypeService {

    private final Logger log = LoggerFactory.getLogger(PetTypeServiceImpl.class);

    private final PetTypeRepository petTypeRepository;

    public PetTypeServiceImpl(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    public PetType save(PetType petType) {
        log.debug("Request to save PetType : {}", petType);
        return petTypeRepository.save(petType);
    }

    @Override
    public PetType update(PetType petType) {
        log.debug("Request to update PetType : {}", petType);
        return petTypeRepository.save(petType);
    }

    @Override
    public Optional<PetType> partialUpdate(PetType petType) {
        log.debug("Request to partially update PetType : {}", petType);

        return petTypeRepository
            .findById(petType.getId())
            .map(existingPetType -> {
                if (petType.getName() != null) {
                    existingPetType.setName(petType.getName());
                }

                return existingPetType;
            })
            .map(petTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetType> findAll() {
        log.debug("Request to get all PetTypes");
        return petTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetType> findOne(Long id) {
        log.debug("Request to get PetType : {}", id);
        return petTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PetType : {}", id);
        petTypeRepository.deleteById(id);
    }
}
