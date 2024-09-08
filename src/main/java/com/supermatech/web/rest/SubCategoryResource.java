package com.supermatech.web.rest;

import com.supermatech.domain.SubCategory;
import com.supermatech.service.SubCategoryService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.supermatech.domain.SubCategory}.
 */
@RestController
@RequestMapping("/api/sub-categories")
public class SubCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubCategoryResource.class);

    private final SubCategoryService subCategoryService;

    public SubCategoryResource(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    /**
     * {@code GET  /sub-categories} : get all the subCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subCategories in body.
     */
    @GetMapping("")
    public List<SubCategory> getAllSubCategories() {
        LOG.debug("REST request to get all SubCategories");
        return subCategoryService.findAll();
    }

    /**
     * {@code GET  /sub-categories/:id} : get the "id" subCategory.
     *
     * @param id the id of the subCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubCategory : {}", id);
        Optional<SubCategory> subCategory = subCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subCategory);
    }
}
