package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Departement;
import com.mycompany.myapp.domain.Etudiant;
import com.mycompany.myapp.repository.DepartementRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.DepartementNameAlreadyUsedException;
import com.mycompany.myapp.web.rest.errors.NumInscriptionAlreadyUsedException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Departement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DepartementResource {

    private final Logger log = LoggerFactory.getLogger(DepartementResource.class);

    private static final String ENTITY_NAME = "departement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartementRepository departementRepository;

    public DepartementResource(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    /**
     * {@code POST  /departements} : Create a new departement.
     *
     * @param departement the departement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departement, or with status {@code 400 (Bad Request)} if the departement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departements")
    public ResponseEntity<Departement> createDepartement(@Valid @RequestBody Departement departement) throws URISyntaxException {
        log.debug("REST request to save Departement : {}", departement);

        // Vérifier si un departement avec le même nom existe déjà
        Optional<Departement> existingDepart = departementRepository.findByDepartmentName(departement.getDepartmentName());
        if (existingDepart.isPresent()) {
            throw new DepartementNameAlreadyUsedException();
        }

        if (departement.getId() != null) {
            throw new BadRequestAlertException("A new departement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Departement result = departementRepository.save(departement);
        return ResponseEntity
            .created(new URI("/api/departements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departements/:id} : Updates an existing departement.
     *
     * @param id the id of the departement to save.
     * @param departement the departement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departement,
     * or with status {@code 400 (Bad Request)} if the departement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departements/{id}")
    public ResponseEntity<Departement> updateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Departement departement
    ) throws URISyntaxException {
        log.debug("REST request to update Departement : {}, {}", id, departement);
        if (departement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Departement result = departementRepository.save(departement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /departements/:id} : Partial updates given fields of an existing departement, field will ignore if it is null
     *
     * @param id the id of the departement to save.
     * @param departement the departement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departement,
     * or with status {@code 400 (Bad Request)} if the departement is not valid,
     * or with status {@code 404 (Not Found)} if the departement is not found,
     * or with status {@code 500 (Internal Server Error)} if the departement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/departements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Departement> partialUpdateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Departement departement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Departement partially : {}, {}", id, departement);
        if (departement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Departement> result = departementRepository
            .findById(departement.getId())
            .map(existingDepartement -> {
                if (departement.getDepartmentName() != null) {
                    existingDepartement.setDepartmentName(departement.getDepartmentName());
                }

                return existingDepartement;
            })
            .map(departementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departement.getId().toString())
        );
    }

    /**
     * {@code GET  /departements} : get all the departements.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departements in body.
     */
    @GetMapping("/departements")
    public List<Departement> getAllDepartements(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Departements");
        if (eagerload) {
            return departementRepository.findAllWithEagerRelationships();
        } else {
            return departementRepository.findAll();
        }
    }

    /**
     * {@code GET  /departements/:id} : get the "id" departement.
     *
     * @param id the id of the departement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departements/{id}")
    public ResponseEntity<Departement> getDepartement(@PathVariable Long id) {
        log.debug("REST request to get Departement : {}", id);
        Optional<Departement> departement = departementRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(departement);
    }

    /**
     * {@code DELETE  /departements/:id} : delete the "id" departement.
     *
     * @param id the id of the departement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departements/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        log.debug("REST request to delete Departement : {}", id);
        departementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
