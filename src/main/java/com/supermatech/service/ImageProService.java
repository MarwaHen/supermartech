package com.supermatech.service;

import com.supermatech.domain.ImagePro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.supermatech.domain.ImagePro}.
 */
public interface ImageProService {
    /**
     * Save a imagePro.
     *
     * @param imagePro the entity to save.
     * @return the persisted entity.
     */
    ImagePro save(ImagePro imagePro);

    /**
     * Updates a imagePro.
     *
     * @param imagePro the entity to update.
     * @return the persisted entity.
     */
    ImagePro update(ImagePro imagePro);

    /**
     * Partially updates a imagePro.
     *
     * @param imagePro the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImagePro> partialUpdate(ImagePro imagePro);

    /**
     * Get all the imagePros.
     *
     * @return the list of entities.
     */
    List<ImagePro> findAll();

    /**
     * Get all the imagePros with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImagePro> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" imagePro.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImagePro> findOne(Long id);

    /**
     * Delete the "id" imagePro.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
