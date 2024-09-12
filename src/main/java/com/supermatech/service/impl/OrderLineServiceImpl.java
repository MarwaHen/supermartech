package com.supermatech.service.impl;

import com.supermatech.domain.OrderLine;
import com.supermatech.repository.OrderLineRepository;
import com.supermatech.service.OrderLineService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.supermatech.domain.OrderLine}.
 */
@Service
@Transactional
public class OrderLineServiceImpl implements OrderLineService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderLineServiceImpl.class);

    private final OrderLineRepository orderLineRepository;

    public OrderLineServiceImpl(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }

    @Override
    public OrderLine save(OrderLine orderLine) {
        LOG.debug("Request to save OrderLine : {}", orderLine);
        return orderLineRepository.save(orderLine);
    }

    @Override
    public OrderLine update(OrderLine orderLine) {
        LOG.debug("Request to update OrderLine : {}", orderLine);
        return orderLineRepository.save(orderLine);
    }

    @Override
    public Optional<OrderLine> partialUpdate(OrderLine orderLine) {
        LOG.debug("Request to partially update OrderLine : {}", orderLine);

        return orderLineRepository
            .findById(orderLine.getId())
            .map(existingOrderLine -> {
                if (orderLine.getOdl_itemquantity() != null) {
                    existingOrderLine.setOdl_itemquantity(orderLine.getOdl_itemquantity());
                }
                if (orderLine.getOdl_soldprice() != null) {
                    existingOrderLine.setOdl_soldprice(orderLine.getOdl_soldprice());
                }

                return existingOrderLine;
            })
            .map(orderLineRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderLine> findAll() {
        LOG.debug("Request to get all OrderLines");
        return orderLineRepository.findAll();
    }

    public Page<OrderLine> findAllWithEagerRelationships(Pageable pageable) {
        return orderLineRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderLine> findOne(Long id) {
        LOG.debug("Request to get OrderLine : {}", id);
        return orderLineRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderLine : {}", id);
        orderLineRepository.deleteById(id);
    }
}
