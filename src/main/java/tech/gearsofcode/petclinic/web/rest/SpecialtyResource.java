package tech.gearsofcode.petclinic.web.rest;

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
import tech.gearsofcode.petclinic.domain.Specialty;
import tech.gearsofcode.petclinic.repository.SpecialtyRepository;
import tech.gearsofcode.petclinic.service.SpecialtyService;
import tech.gearsofcode.petclinic.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.gearsofcode.petclinic.domain.Specialty}.
 */
@RestController
@RequestMapping("/api")
public class SpecialtyResource {

    private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);

    private static final String ENTITY_NAME = "specialty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialtyService specialtyService;

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyResource(SpecialtyService specialtyService, SpecialtyRepository specialtyRepository) {
        this.specialtyService = specialtyService;
        this.specialtyRepository = specialtyRepository;
    }

    /**
     * {@code POST  /specialties} : Create a new specialty.
     *
     * @param specialty the specialty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialty, or with status {@code 400 (Bad Request)} if the specialty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specialties")
    public ResponseEntity<Specialty> createSpecialty(@RequestBody Specialty specialty) throws URISyntaxException {
        log.debug("REST request to save Specialty : {}", specialty);
        if (specialty.getId() != null) {
            throw new BadRequestAlertException("A new specialty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Specialty result = specialtyService.save(specialty);
        return ResponseEntity
            .created(new URI("/api/specialties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specialties/:id} : Updates an existing specialty.
     *
     * @param id the id of the specialty to save.
     * @param specialty the specialty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialty,
     * or with status {@code 400 (Bad Request)} if the specialty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specialties/{id}")
    public ResponseEntity<Specialty> updateSpecialty(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Specialty specialty
    ) throws URISyntaxException {
        log.debug("REST request to update Specialty : {}, {}", id, specialty);
        if (specialty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialtyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Specialty result = specialtyService.update(specialty);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specialty.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /specialties/:id} : Partial updates given fields of an existing specialty, field will ignore if it is null
     *
     * @param id the id of the specialty to save.
     * @param specialty the specialty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialty,
     * or with status {@code 400 (Bad Request)} if the specialty is not valid,
     * or with status {@code 404 (Not Found)} if the specialty is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/specialties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Specialty> partialUpdateSpecialty(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Specialty specialty
    ) throws URISyntaxException {
        log.debug("REST request to partial update Specialty partially : {}, {}", id, specialty);
        if (specialty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialtyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Specialty> result = specialtyService.partialUpdate(specialty);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specialty.getId().toString())
        );
    }

    /**
     * {@code GET  /specialties} : get all the specialties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialties in body.
     */
    @GetMapping("/specialties")
    public List<Specialty> getAllSpecialties() {
        log.debug("REST request to get all Specialties");
        return specialtyService.findAll();
    }

    /**
     * {@code GET  /specialties/:id} : get the "id" specialty.
     *
     * @param id the id of the specialty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specialties/{id}")
    public ResponseEntity<Specialty> getSpecialty(@PathVariable Long id) {
        log.debug("REST request to get Specialty : {}", id);
        Optional<Specialty> specialty = specialtyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialty);
    }

    /**
     * {@code DELETE  /specialties/:id} : delete the "id" specialty.
     *
     * @param id the id of the specialty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specialties/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        log.debug("REST request to delete Specialty : {}", id);
        specialtyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
