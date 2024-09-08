package com.supermatech.service.impl;

import com.supermatech.domain.SubCategory;
import com.supermatech.repository.SubCategoryRepository;
import com.supermatech.service.SubCategoryService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.supermatech.domain.SubCategory}.
 */
@Service
@Transactional
public class SubCategoryServiceImpl implements SubCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(SubCategoryServiceImpl.class);

    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    @Override
    public SubCategory save(SubCategory subCategory) {
        LOG.debug("Request to save SubCategory : {}", subCategory);
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public SubCategory update(SubCategory subCategory) {
        LOG.debug("Request to update SubCategory : {}", subCategory);
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public Optional<SubCategory> partialUpdate(SubCategory subCategory) {
        LOG.debug("Request to partially update SubCategory : {}", subCategory);

        return subCategoryRepository
            .findById(subCategory.getId())
            .map(existingSubCategory -> {
                if (subCategory.getCatt_name() != null) {
                    existingSubCategory.setCatt_name(subCategory.getCatt_name());
                }
                if (subCategory.getCatt_description() != null) {
                    existingSubCategory.setCatt_description(subCategory.getCatt_description());
                }
                if (subCategory.getCat_id() != null) {
                    existingSubCategory.setCat_id(subCategory.getCat_id());
                }
                if (subCategory.getCatt_icon() != null) {
                    existingSubCategory.setCatt_icon(subCategory.getCatt_icon());
                }

                return existingSubCategory;
            })
            .map(subCategoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategory> findAll() {
        LOG.debug("Request to get all SubCategories");
        return subCategoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubCategory> findOne(Long id) {
        LOG.debug("Request to get SubCategory : {}", id);
        return subCategoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SubCategory : {}", id);
        subCategoryRepository.deleteById(id);
    }
}
