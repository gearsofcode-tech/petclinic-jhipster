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
import tech.gearsofcode.petclinic.domain.Vet;
import tech.gearsofcode.petclinic.repository.VetRepository;
import tech.gearsofcode.petclinic.service.VetService;
import tech.gearsofcode.petclinic.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.gearsofcode.petclinic.domain.Vet}.
 */
@RestController
@RequestMapping("/api")
public class VetResource {

    private final Logger log = LoggerFactory.getLogger(VetResource.class);

    private static final String ENTITY_NAME = "vet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VetService vetService;

    private final VetRepository vetRepository;

    public VetResource(VetService vetService, VetRepository vetRepository) {
        this.vetService = vetService;
        this.vetRepository = vetRepository;
    }

    /**
     * {@code POST  /vets} : Create a new vet.
     *
     * @param vet the vet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vet, or with status {@code 400 (Bad Request)} if the vet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vets")
    public ResponseEntity<Vet> createVet(@RequestBody Vet vet) throws URISyntaxException {
        log.debug("REST request to save Vet : {}", vet);
        if (vet.getId() != null) {
            throw new BadRequestAlertException("A new vet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vet result = vetService.save(vet);
        return ResponseEntity
            .created(new URI("/api/vets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vets/:id} : Updates an existing vet.
     *
     * @param id the id of the vet to save.
     * @param vet the vet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vet,
     * or with status {@code 400 (Bad Request)} if the vet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vets/{id}")
    public ResponseEntity<Vet> updateVet(@PathVariable(value = "id", required = false) final Long id, @RequestBody Vet vet)
        throws URISyntaxException {
        log.debug("REST request to update Vet : {}, {}", id, vet);
        if (vet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vet result = vetService.update(vet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vets/:id} : Partial updates given fields of an existing vet, field will ignore if it is null
     *
     * @param id the id of the vet to save.
     * @param vet the vet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vet,
     * or with status {@code 400 (Bad Request)} if the vet is not valid,
     * or with status {@code 404 (Not Found)} if the vet is not found,
     * or with status {@code 500 (Internal Server Error)} if the vet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vet> partialUpdateVet(@PathVariable(value = "id", required = false) final Long id, @RequestBody Vet vet)
        throws URISyntaxException {
        log.debug("REST request to partial update Vet partially : {}, {}", id, vet);
        if (vet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vet> result = vetService.partialUpdate(vet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vet.getId().toString())
        );
    }

    /**
     * {@code GET  /vets} : get all the vets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vets in body.
     */
    @GetMapping("/vets")
    public List<Vet> getAllVets(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Vets");
        return vetService.findAll();
    }

    /**
     * {@code GET  /vets/:id} : get the "id" vet.
     *
     * @param id the id of the vet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vets/{id}")
    public ResponseEntity<Vet> getVet(@PathVariable Long id) {
        log.debug("REST request to get Vet : {}", id);
        Optional<Vet> vet = vetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vet);
    }

    /**
     * {@code DELETE  /vets/:id} : delete the "id" vet.
     *
     * @param id the id of the vet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vets/{id}")
    public ResponseEntity<Void> deleteVet(@PathVariable Long id) {
        log.debug("REST request to delete Vet : {}", id);
        vetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
