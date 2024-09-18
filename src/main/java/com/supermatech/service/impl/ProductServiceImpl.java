package com.supermatech.service.impl;

import com.supermatech.domain.Product;
import com.supermatech.repository.ProductRepository;
import com.supermatech.service.ProductService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.supermatech.domain.Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        LOG.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        LOG.debug("Request to update Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> partialUpdate(Product product) {
        LOG.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getPro_name() != null) {
                    existingProduct.setPro_name(product.getPro_name());
                }
                if (product.getPro_description() != null) {
                    existingProduct.setPro_description(product.getPro_description());
                }
                if (product.getPro_price() != null) {
                    existingProduct.setPro_price(product.getPro_price());
                }
                if (product.getPro_quantity() != null) {
                    existingProduct.setPro_quantity(product.getPro_quantity());
                }
                if (product.getCatt_id() != null) {
                    existingProduct.setCatt_id(product.getCatt_id());
                }
                if (product.getPro_date() != null) {
                    existingProduct.setPro_date(product.getPro_date());
                }
                if (product.getPro_promotion() != null) {
                    existingProduct.setPro_promotion(product.getPro_promotion());
                }
                if (product.getPro_mark() != null) {
                    existingProduct.setPro_mark(product.getPro_mark());
                }

                return existingProduct;
            })
            .map(productRepository::save);
    }

    public Page<Product> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        LOG.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
