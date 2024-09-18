package com.supermatech.service;

import com.supermatech.domain.OrderLine;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.supermatech.domain.OrderLine}.
 */
public interface OrderLineService {
    /**
     * Save a orderLine.
     *
     * @param orderLine the entity to save.
     * @return the persisted entity.
     */
    OrderLine save(OrderLine orderLine);

    /**
     * Updates a orderLine.
     *
     * @param orderLine the entity to update.
     * @return the persisted entity.
     */
    OrderLine update(OrderLine orderLine);

    /**
     * Partially updates a orderLine.
     *
     * @param orderLine the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderLine> partialUpdate(OrderLine orderLine);

    /**
     * Get all the orderLines.
     *
     * @return the list of entities.
     */
    List<OrderLine> findAll();

    /**
     * Get all the orderLines with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderLine> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orderLine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderLine> findOne(Long id);

    /**
     * Delete the "id" orderLine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
