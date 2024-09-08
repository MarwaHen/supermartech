package com.supermatech.web.rest;

import com.supermatech.domain.ImagePro;
import com.supermatech.repository.ImageProRepository;
import com.supermatech.service.ImageProService;
import com.supermatech.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.supermatech.domain.ImagePro}.
 */
@RestController
@RequestMapping("/api/image-pros")
public class ImageProResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImageProResource.class);

    private static final String ENTITY_NAME = "imagePro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageProService imageProService;

    private final ImageProRepository imageProRepository;

    public ImageProResource(ImageProService imageProService, ImageProRepository imageProRepository) {
        this.imageProService = imageProService;
        this.imageProRepository = imageProRepository;
    }

    /**
     * {@code POST  /image-pros} : Create a new imagePro.
     *
     * @param imagePro the imagePro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imagePro, or with status {@code 400 (Bad Request)} if the imagePro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImagePro> createImagePro(@Valid @RequestBody ImagePro imagePro) throws URISyntaxException {
        LOG.debug("REST request to save ImagePro : {}", imagePro);
        if (imagePro.getId() != null) {
            throw new BadRequestAlertException("A new imagePro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        imagePro = imageProService.save(imagePro);
        return ResponseEntity.created(new URI("/api/image-pros/" + imagePro.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, imagePro.getId().toString()))
            .body(imagePro);
    }

    /**
     * {@code PUT  /image-pros/:id} : Updates an existing imagePro.
     *
     * @param id the id of the imagePro to save.
     * @param imagePro the imagePro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagePro,
     * or with status {@code 400 (Bad Request)} if the imagePro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imagePro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImagePro> updateImagePro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImagePro imagePro
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImagePro : {}, {}", id, imagePro);
        if (imagePro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagePro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageProRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        imagePro = imageProService.update(imagePro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagePro.getId().toString()))
            .body(imagePro);
    }

    /**
     * {@code PATCH  /image-pros/:id} : Partial updates given fields of an existing imagePro, field will ignore if it is null
     *
     * @param id the id of the imagePro to save.
     * @param imagePro the imagePro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagePro,
     * or with status {@code 400 (Bad Request)} if the imagePro is not valid,
     * or with status {@code 404 (Not Found)} if the imagePro is not found,
     * or with status {@code 500 (Internal Server Error)} if the imagePro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImagePro> partialUpdateImagePro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImagePro imagePro
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImagePro partially : {}, {}", id, imagePro);
        if (imagePro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagePro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageProRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImagePro> result = imageProService.partialUpdate(imagePro);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagePro.getId().toString())
        );
    }

    /**
     * {@code GET  /image-pros} : get all the imagePros.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imagePros in body.
     */
    @GetMapping("")
    public List<ImagePro> getAllImagePros(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all ImagePros");
        return imageProService.findAll();
    }

    /**
     * {@code GET  /image-pros/:id} : get the "id" imagePro.
     *
     * @param id the id of the imagePro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imagePro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImagePro> getImagePro(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImagePro : {}", id);
        Optional<ImagePro> imagePro = imageProService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imagePro);
    }

    /**
     * {@code DELETE  /image-pros/:id} : delete the "id" imagePro.
     *
     * @param id the id of the imagePro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImagePro(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImagePro : {}", id);
        imageProService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
