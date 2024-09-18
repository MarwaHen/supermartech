package com.supermatech.service;

import com.supermatech.domain.SubCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.supermatech.domain.SubCategory}.
 */
public interface SubCategoryService {
    /**
     * Save a subCategory.
     *
     * @param subCategory the entity to save.
     * @return the persisted entity.
     */
    SubCategory save(SubCategory subCategory);

    /**
     * Updates a subCategory.
     *
     * @param subCategory the entity to update.
     * @return the persisted entity.
     */
    SubCategory update(SubCategory subCategory);

    /**
     * Partially updates a subCategory.
     *
     * @param subCategory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubCategory> partialUpdate(SubCategory subCategory);

    /**
     * Get all the subCategories.
     *
     * @return the list of entities.
     */
    List<SubCategory> findAll();

    /**
     * Get all the subCategories with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubCategory> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" subCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubCategory> findOne(Long id);

    /**
     * Delete the "id" subCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
