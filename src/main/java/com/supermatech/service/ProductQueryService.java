package com.supermatech.service;

import com.supermatech.domain.*; // for static metamodels
import com.supermatech.domain.Product;
import com.supermatech.repository.ProductRepository;
import com.supermatech.service.criteria.ProductCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Product} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    public ProductQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Return a {@link Page} of {@link Product} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Product> findByCriteria(ProductCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getPro_name() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPro_name(), Product_.pro_name));
            }
            if (criteria.getPro_description() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPro_description(), Product_.pro_description));
            }
            if (criteria.getPro_price() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPro_price(), Product_.pro_price));
            }
            if (criteria.getPro_quantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPro_quantity(), Product_.pro_quantity));
            }
            if (criteria.getCatt_id() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCatt_id(), Product_.catt_id));
            }
            if (criteria.getPro_date() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPro_date(), Product_.pro_date));
            }
            if (criteria.getPro_promotion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPro_promotion(), Product_.pro_promotion));
            }
            if (criteria.getPro_mark() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPro_mark(), Product_.pro_mark));
            }
            if (criteria.getSubCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSubCategoryId(), root ->
                        root.join(Product_.subCategory, JoinType.LEFT).get(SubCategory_.id)
                    )
                );
            }
        }
        return specification;
    }
}
