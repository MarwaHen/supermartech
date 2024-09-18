package com.supermatech.web.rest;

import static com.supermatech.domain.OrderLineAsserts.*;
import static com.supermatech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermatech.IntegrationTest;
import com.supermatech.domain.Order;
import com.supermatech.domain.OrderLine;
import com.supermatech.domain.Product;
import com.supermatech.repository.OrderLineRepository;
import com.supermatech.service.OrderLineService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderLineResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderLineResourceIT {

    private static final Integer DEFAULT_ODL_ITEMQUANTITY = 1;
    private static final Integer UPDATED_ODL_ITEMQUANTITY = 2;

    private static final Double DEFAULT_ODL_SOLDPRICE = 1D;
    private static final Double UPDATED_ODL_SOLDPRICE = 2D;

    private static final String ENTITY_API_URL = "/api/order-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Mock
    private OrderLineRepository orderLineRepositoryMock;

    @Mock
    private OrderLineService orderLineServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderLineMockMvc;

    private OrderLine orderLine;

    private OrderLine insertedOrderLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderLine createEntity(EntityManager em) {
        OrderLine orderLine = new OrderLine().odl_itemquantity(DEFAULT_ODL_ITEMQUANTITY).odl_soldprice(DEFAULT_ODL_SOLDPRICE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        orderLine.setProduct(product);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        orderLine.setOrder(order);
        return orderLine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderLine createUpdatedEntity(EntityManager em) {
        OrderLine updatedOrderLine = new OrderLine().odl_itemquantity(UPDATED_ODL_ITEMQUANTITY).odl_soldprice(UPDATED_ODL_SOLDPRICE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedOrderLine.setProduct(product);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createUpdatedEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        updatedOrderLine.setOrder(order);
        return updatedOrderLine;
    }

    @BeforeEach
    public void initTest() {
        orderLine = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrderLine != null) {
            orderLineRepository.delete(insertedOrderLine);
            insertedOrderLine = null;
        }
    }

    @Test
    @Transactional
    void createOrderLine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderLine
        var returnedOrderLine = om.readValue(
            restOrderLineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderLine)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderLine.class
        );

        // Validate the OrderLine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderLineUpdatableFieldsEquals(returnedOrderLine, getPersistedOrderLine(returnedOrderLine));

        insertedOrderLine = returnedOrderLine;
    }

    @Test
    @Transactional
    void createOrderLineWithExistingId() throws Exception {
        // Create the OrderLine with an existing ID
        orderLine.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderLine)))
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOdl_itemquantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderLine.setOdl_itemquantity(null);

        // Create the OrderLine, which fails.

        restOrderLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderLine)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOdl_soldpriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        orderLine.setOdl_soldprice(null);

        // Create the OrderLine, which fails.

        restOrderLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderLine)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderLines() throws Exception {
        // Initialize the database
        insertedOrderLine = orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList
        restOrderLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].odl_itemquantity").value(hasItem(DEFAULT_ODL_ITEMQUANTITY)))
            .andExpect(jsonPath("$.[*].odl_soldprice").value(hasItem(DEFAULT_ODL_SOLDPRICE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderLinesWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderLineServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderLineMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderLineServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderLinesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderLineServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderLineMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderLineRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderLine() throws Exception {
        // Initialize the database
        insertedOrderLine = orderLineRepository.saveAndFlush(orderLine);

        // Get the orderLine
        restOrderLineMockMvc
            .perform(get(ENTITY_API_URL_ID, orderLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderLine.getId().intValue()))
            .andExpect(jsonPath("$.odl_itemquantity").value(DEFAULT_ODL_ITEMQUANTITY))
            .andExpect(jsonPath("$.odl_soldprice").value(DEFAULT_ODL_SOLDPRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrderLine() throws Exception {
        // Get the orderLine
        restOrderLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderLine() throws Exception {
        // Initialize the database
        insertedOrderLine = orderLineRepository.saveAndFlush(orderLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderLine
        OrderLine updatedOrderLine = orderLineRepository.findById(orderLine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderLine are not directly saved in db
        em.detach(updatedOrderLine);
        updatedOrderLine.odl_itemquantity(UPDATED_ODL_ITEMQUANTITY).odl_soldprice(UPDATED_ODL_SOLDPRICE);

        restOrderLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderLine))
            )
            .andExpect(status().isOk());

        // Validate the OrderLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderLineToMatchAllProperties(updatedOrderLine);
    }

    @Test
    @Transactional
    void putNonExistingOrderLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderLine.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderLineWithPatch() throws Exception {
        // Initialize the database
        insertedOrderLine = orderLineRepository.saveAndFlush(orderLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderLine using partial update
        OrderLine partialUpdatedOrderLine = new OrderLine();
        partialUpdatedOrderLine.setId(orderLine.getId());

        restOrderLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderLine))
            )
            .andExpect(status().isOk());

        // Validate the OrderLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderLineUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrderLine, orderLine),
            getPersistedOrderLine(orderLine)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrderLineWithPatch() throws Exception {
        // Initialize the database
        insertedOrderLine = orderLineRepository.saveAndFlush(orderLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderLine using partial update
        OrderLine partialUpdatedOrderLine = new OrderLine();
        partialUpdatedOrderLine.setId(orderLine.getId());

        partialUpdatedOrderLine.odl_itemquantity(UPDATED_ODL_ITEMQUANTITY).odl_soldprice(UPDATED_ODL_SOLDPRICE);

        restOrderLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderLine))
            )
            .andExpect(status().isOk());

        // Validate the OrderLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderLineUpdatableFieldsEquals(partialUpdatedOrderLine, getPersistedOrderLine(partialUpdatedOrderLine));
    }

    @Test
    @Transactional
    void patchNonExistingOrderLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderLine() throws Exception {
        // Initialize the database
        insertedOrderLine = orderLineRepository.saveAndFlush(orderLine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderLine
        restOrderLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderLineRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected OrderLine getPersistedOrderLine(OrderLine orderLine) {
        return orderLineRepository.findById(orderLine.getId()).orElseThrow();
    }

    protected void assertPersistedOrderLineToMatchAllProperties(OrderLine expectedOrderLine) {
        assertOrderLineAllPropertiesEquals(expectedOrderLine, getPersistedOrderLine(expectedOrderLine));
    }

    protected void assertPersistedOrderLineToMatchUpdatableProperties(OrderLine expectedOrderLine) {
        assertOrderLineAllUpdatablePropertiesEquals(expectedOrderLine, getPersistedOrderLine(expectedOrderLine));
    }
}
