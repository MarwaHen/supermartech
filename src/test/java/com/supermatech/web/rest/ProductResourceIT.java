package com.supermatech.web.rest;

import static com.supermatech.domain.ProductAsserts.*;
import static com.supermatech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermatech.IntegrationTest;
import com.supermatech.domain.Product;
import com.supermatech.domain.SubCategory;
import com.supermatech.repository.ProductRepository;
import com.supermatech.service.ProductService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_PRO_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRO_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRO_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRO_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRO_PRICE = 1D;
    private static final Double UPDATED_PRO_PRICE = 2D;
    private static final Double SMALLER_PRO_PRICE = 1D - 1D;

    private static final Integer DEFAULT_PRO_QUANTITY = 1;
    private static final Integer UPDATED_PRO_QUANTITY = 2;
    private static final Integer SMALLER_PRO_QUANTITY = 1 - 1;

    private static final Integer DEFAULT_CATT_ID = 1;
    private static final Integer UPDATED_CATT_ID = 2;
    private static final Integer SMALLER_CATT_ID = 1 - 1;

    private static final Instant DEFAULT_PRO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_PRO_PROMOTION = 1;
    private static final Integer UPDATED_PRO_PROMOTION = 2;
    private static final Integer SMALLER_PRO_PROMOTION = 1 - 1;

    private static final String DEFAULT_PRO_MARK = "AAAAAAAAAA";
    private static final String UPDATED_PRO_MARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    private Product insertedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .pro_name(DEFAULT_PRO_NAME)
            .pro_description(DEFAULT_PRO_DESCRIPTION)
            .pro_price(DEFAULT_PRO_PRICE)
            .pro_quantity(DEFAULT_PRO_QUANTITY)
            .catt_id(DEFAULT_CATT_ID)
            .pro_date(DEFAULT_PRO_DATE)
            .pro_promotion(DEFAULT_PRO_PROMOTION)
            .pro_mark(DEFAULT_PRO_MARK);
        // Add required entity
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            subCategory = SubCategoryResourceIT.createEntity(em);
            em.persist(subCategory);
            em.flush();
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        product.setSubCategory(subCategory);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product updatedProduct = new Product()
            .pro_name(UPDATED_PRO_NAME)
            .pro_description(UPDATED_PRO_DESCRIPTION)
            .pro_price(UPDATED_PRO_PRICE)
            .pro_quantity(UPDATED_PRO_QUANTITY)
            .catt_id(UPDATED_CATT_ID)
            .pro_date(UPDATED_PRO_DATE)
            .pro_promotion(UPDATED_PRO_PROMOTION)
            .pro_mark(UPDATED_PRO_MARK);
        // Add required entity
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            subCategory = SubCategoryResourceIT.createUpdatedEntity(em);
            em.persist(subCategory);
            em.flush();
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        updatedProduct.setSubCategory(subCategory);
        return updatedProduct;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct);
            insertedProduct = null;
        }
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Product
        var returnedProduct = om.readValue(
            restProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Product.class
        );

        // Validate the Product in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductUpdatableFieldsEquals(returnedProduct, getPersistedProduct(returnedProduct));

        insertedProduct = returnedProduct;
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPro_nameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setPro_name(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPro_priceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setPro_price(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPro_quantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setPro_quantity(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCatt_idIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setCatt_id(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].pro_name").value(hasItem(DEFAULT_PRO_NAME)))
            .andExpect(jsonPath("$.[*].pro_description").value(hasItem(DEFAULT_PRO_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pro_price").value(hasItem(DEFAULT_PRO_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].pro_quantity").value(hasItem(DEFAULT_PRO_QUANTITY)))
            .andExpect(jsonPath("$.[*].catt_id").value(hasItem(DEFAULT_CATT_ID)))
            .andExpect(jsonPath("$.[*].pro_date").value(hasItem(DEFAULT_PRO_DATE.toString())))
            .andExpect(jsonPath("$.[*].pro_promotion").value(hasItem(DEFAULT_PRO_PROMOTION)))
            .andExpect(jsonPath("$.[*].pro_mark").value(hasItem(DEFAULT_PRO_MARK)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.pro_name").value(DEFAULT_PRO_NAME))
            .andExpect(jsonPath("$.pro_description").value(DEFAULT_PRO_DESCRIPTION))
            .andExpect(jsonPath("$.pro_price").value(DEFAULT_PRO_PRICE.doubleValue()))
            .andExpect(jsonPath("$.pro_quantity").value(DEFAULT_PRO_QUANTITY))
            .andExpect(jsonPath("$.catt_id").value(DEFAULT_CATT_ID))
            .andExpect(jsonPath("$.pro_date").value(DEFAULT_PRO_DATE.toString()))
            .andExpect(jsonPath("$.pro_promotion").value(DEFAULT_PRO_PROMOTION))
            .andExpect(jsonPath("$.pro_mark").value(DEFAULT_PRO_MARK));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByPro_nameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_name equals to
        defaultProductFiltering("pro_name.equals=" + DEFAULT_PRO_NAME, "pro_name.equals=" + UPDATED_PRO_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByPro_nameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_name in
        defaultProductFiltering("pro_name.in=" + DEFAULT_PRO_NAME + "," + UPDATED_PRO_NAME, "pro_name.in=" + UPDATED_PRO_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByPro_nameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_name is not null
        defaultProductFiltering("pro_name.specified=true", "pro_name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPro_nameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_name contains
        defaultProductFiltering("pro_name.contains=" + DEFAULT_PRO_NAME, "pro_name.contains=" + UPDATED_PRO_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByPro_nameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_name does not contain
        defaultProductFiltering("pro_name.doesNotContain=" + UPDATED_PRO_NAME, "pro_name.doesNotContain=" + DEFAULT_PRO_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByPro_descriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_description equals to
        defaultProductFiltering("pro_description.equals=" + DEFAULT_PRO_DESCRIPTION, "pro_description.equals=" + UPDATED_PRO_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByPro_descriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_description in
        defaultProductFiltering(
            "pro_description.in=" + DEFAULT_PRO_DESCRIPTION + "," + UPDATED_PRO_DESCRIPTION,
            "pro_description.in=" + UPDATED_PRO_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByPro_descriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_description is not null
        defaultProductFiltering("pro_description.specified=true", "pro_description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPro_descriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_description contains
        defaultProductFiltering(
            "pro_description.contains=" + DEFAULT_PRO_DESCRIPTION,
            "pro_description.contains=" + UPDATED_PRO_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByPro_descriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_description does not contain
        defaultProductFiltering(
            "pro_description.doesNotContain=" + UPDATED_PRO_DESCRIPTION,
            "pro_description.doesNotContain=" + DEFAULT_PRO_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByPro_priceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_price equals to
        defaultProductFiltering("pro_price.equals=" + DEFAULT_PRO_PRICE, "pro_price.equals=" + UPDATED_PRO_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPro_priceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_price in
        defaultProductFiltering("pro_price.in=" + DEFAULT_PRO_PRICE + "," + UPDATED_PRO_PRICE, "pro_price.in=" + UPDATED_PRO_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPro_priceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_price is not null
        defaultProductFiltering("pro_price.specified=true", "pro_price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPro_priceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_price is greater than or equal to
        defaultProductFiltering("pro_price.greaterThanOrEqual=" + DEFAULT_PRO_PRICE, "pro_price.greaterThanOrEqual=" + UPDATED_PRO_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPro_priceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_price is less than or equal to
        defaultProductFiltering("pro_price.lessThanOrEqual=" + DEFAULT_PRO_PRICE, "pro_price.lessThanOrEqual=" + SMALLER_PRO_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPro_priceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_price is less than
        defaultProductFiltering("pro_price.lessThan=" + UPDATED_PRO_PRICE, "pro_price.lessThan=" + DEFAULT_PRO_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPro_priceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_price is greater than
        defaultProductFiltering("pro_price.greaterThan=" + SMALLER_PRO_PRICE, "pro_price.greaterThan=" + DEFAULT_PRO_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPro_quantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_quantity equals to
        defaultProductFiltering("pro_quantity.equals=" + DEFAULT_PRO_QUANTITY, "pro_quantity.equals=" + UPDATED_PRO_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByPro_quantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_quantity in
        defaultProductFiltering(
            "pro_quantity.in=" + DEFAULT_PRO_QUANTITY + "," + UPDATED_PRO_QUANTITY,
            "pro_quantity.in=" + UPDATED_PRO_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByPro_quantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_quantity is not null
        defaultProductFiltering("pro_quantity.specified=true", "pro_quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPro_quantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_quantity is greater than or equal to
        defaultProductFiltering(
            "pro_quantity.greaterThanOrEqual=" + DEFAULT_PRO_QUANTITY,
            "pro_quantity.greaterThanOrEqual=" + UPDATED_PRO_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByPro_quantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_quantity is less than or equal to
        defaultProductFiltering(
            "pro_quantity.lessThanOrEqual=" + DEFAULT_PRO_QUANTITY,
            "pro_quantity.lessThanOrEqual=" + SMALLER_PRO_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByPro_quantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_quantity is less than
        defaultProductFiltering("pro_quantity.lessThan=" + UPDATED_PRO_QUANTITY, "pro_quantity.lessThan=" + DEFAULT_PRO_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByPro_quantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_quantity is greater than
        defaultProductFiltering("pro_quantity.greaterThan=" + SMALLER_PRO_QUANTITY, "pro_quantity.greaterThan=" + DEFAULT_PRO_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByCatt_idIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where catt_id equals to
        defaultProductFiltering("catt_id.equals=" + DEFAULT_CATT_ID, "catt_id.equals=" + UPDATED_CATT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByCatt_idIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where catt_id in
        defaultProductFiltering("catt_id.in=" + DEFAULT_CATT_ID + "," + UPDATED_CATT_ID, "catt_id.in=" + UPDATED_CATT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByCatt_idIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where catt_id is not null
        defaultProductFiltering("catt_id.specified=true", "catt_id.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCatt_idIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where catt_id is greater than or equal to
        defaultProductFiltering("catt_id.greaterThanOrEqual=" + DEFAULT_CATT_ID, "catt_id.greaterThanOrEqual=" + UPDATED_CATT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByCatt_idIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where catt_id is less than or equal to
        defaultProductFiltering("catt_id.lessThanOrEqual=" + DEFAULT_CATT_ID, "catt_id.lessThanOrEqual=" + SMALLER_CATT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByCatt_idIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where catt_id is less than
        defaultProductFiltering("catt_id.lessThan=" + UPDATED_CATT_ID, "catt_id.lessThan=" + DEFAULT_CATT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByCatt_idIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where catt_id is greater than
        defaultProductFiltering("catt_id.greaterThan=" + SMALLER_CATT_ID, "catt_id.greaterThan=" + DEFAULT_CATT_ID);
    }

    @Test
    @Transactional
    void getAllProductsByPro_dateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_date equals to
        defaultProductFiltering("pro_date.equals=" + DEFAULT_PRO_DATE, "pro_date.equals=" + UPDATED_PRO_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByPro_dateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_date in
        defaultProductFiltering("pro_date.in=" + DEFAULT_PRO_DATE + "," + UPDATED_PRO_DATE, "pro_date.in=" + UPDATED_PRO_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByPro_dateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_date is not null
        defaultProductFiltering("pro_date.specified=true", "pro_date.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPro_promotionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_promotion equals to
        defaultProductFiltering("pro_promotion.equals=" + DEFAULT_PRO_PROMOTION, "pro_promotion.equals=" + UPDATED_PRO_PROMOTION);
    }

    @Test
    @Transactional
    void getAllProductsByPro_promotionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_promotion in
        defaultProductFiltering(
            "pro_promotion.in=" + DEFAULT_PRO_PROMOTION + "," + UPDATED_PRO_PROMOTION,
            "pro_promotion.in=" + UPDATED_PRO_PROMOTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByPro_promotionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_promotion is not null
        defaultProductFiltering("pro_promotion.specified=true", "pro_promotion.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPro_promotionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_promotion is greater than or equal to
        defaultProductFiltering(
            "pro_promotion.greaterThanOrEqual=" + DEFAULT_PRO_PROMOTION,
            "pro_promotion.greaterThanOrEqual=" + UPDATED_PRO_PROMOTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByPro_promotionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_promotion is less than or equal to
        defaultProductFiltering(
            "pro_promotion.lessThanOrEqual=" + DEFAULT_PRO_PROMOTION,
            "pro_promotion.lessThanOrEqual=" + SMALLER_PRO_PROMOTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByPro_promotionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_promotion is less than
        defaultProductFiltering("pro_promotion.lessThan=" + UPDATED_PRO_PROMOTION, "pro_promotion.lessThan=" + DEFAULT_PRO_PROMOTION);
    }

    @Test
    @Transactional
    void getAllProductsByPro_promotionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_promotion is greater than
        defaultProductFiltering("pro_promotion.greaterThan=" + SMALLER_PRO_PROMOTION, "pro_promotion.greaterThan=" + DEFAULT_PRO_PROMOTION);
    }

    @Test
    @Transactional
    void getAllProductsByPro_markIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_mark equals to
        defaultProductFiltering("pro_mark.equals=" + DEFAULT_PRO_MARK, "pro_mark.equals=" + UPDATED_PRO_MARK);
    }

    @Test
    @Transactional
    void getAllProductsByPro_markIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_mark in
        defaultProductFiltering("pro_mark.in=" + DEFAULT_PRO_MARK + "," + UPDATED_PRO_MARK, "pro_mark.in=" + UPDATED_PRO_MARK);
    }

    @Test
    @Transactional
    void getAllProductsByPro_markIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_mark is not null
        defaultProductFiltering("pro_mark.specified=true", "pro_mark.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPro_markContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_mark contains
        defaultProductFiltering("pro_mark.contains=" + DEFAULT_PRO_MARK, "pro_mark.contains=" + UPDATED_PRO_MARK);
    }

    @Test
    @Transactional
    void getAllProductsByPro_markNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where pro_mark does not contain
        defaultProductFiltering("pro_mark.doesNotContain=" + UPDATED_PRO_MARK, "pro_mark.doesNotContain=" + DEFAULT_PRO_MARK);
    }

    @Test
    @Transactional
    void getAllProductsBySubCategoryIsEqualToSomething() throws Exception {
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            subCategory = SubCategoryResourceIT.createEntity(em);
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        em.persist(subCategory);
        em.flush();
        product.setSubCategory(subCategory);
        productRepository.saveAndFlush(product);
        Long subCategoryId = subCategory.getId();
        // Get all the productList where subCategory equals to subCategoryId
        defaultProductShouldBeFound("subCategoryId.equals=" + subCategoryId);

        // Get all the productList where subCategory equals to (subCategoryId + 1)
        defaultProductShouldNotBeFound("subCategoryId.equals=" + (subCategoryId + 1));
    }

    private void defaultProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductShouldBeFound(shouldBeFound);
        defaultProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].pro_name").value(hasItem(DEFAULT_PRO_NAME)))
            .andExpect(jsonPath("$.[*].pro_description").value(hasItem(DEFAULT_PRO_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pro_price").value(hasItem(DEFAULT_PRO_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].pro_quantity").value(hasItem(DEFAULT_PRO_QUANTITY)))
            .andExpect(jsonPath("$.[*].catt_id").value(hasItem(DEFAULT_CATT_ID)))
            .andExpect(jsonPath("$.[*].pro_date").value(hasItem(DEFAULT_PRO_DATE.toString())))
            .andExpect(jsonPath("$.[*].pro_promotion").value(hasItem(DEFAULT_PRO_PROMOTION)))
            .andExpect(jsonPath("$.[*].pro_mark").value(hasItem(DEFAULT_PRO_MARK)));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .pro_name(UPDATED_PRO_NAME)
            .pro_description(UPDATED_PRO_DESCRIPTION)
            .pro_price(UPDATED_PRO_PRICE)
            .pro_quantity(UPDATED_PRO_QUANTITY)
            .catt_id(UPDATED_CATT_ID)
            .pro_date(UPDATED_PRO_DATE)
            .pro_promotion(UPDATED_PRO_PROMOTION)
            .pro_mark(UPDATED_PRO_MARK);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductToMatchAllProperties(updatedProduct);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL_ID, product.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.pro_description(UPDATED_PRO_DESCRIPTION).pro_price(UPDATED_PRO_PRICE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduct, product), getPersistedProduct(product));
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .pro_name(UPDATED_PRO_NAME)
            .pro_description(UPDATED_PRO_DESCRIPTION)
            .pro_price(UPDATED_PRO_PRICE)
            .pro_quantity(UPDATED_PRO_QUANTITY)
            .catt_id(UPDATED_CATT_ID)
            .pro_date(UPDATED_PRO_DATE)
            .pro_promotion(UPDATED_PRO_PROMOTION)
            .pro_mark(UPDATED_PRO_MARK);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(partialUpdatedProduct, getPersistedProduct(partialUpdatedProduct));
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, product.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRepository.count();
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

    protected Product getPersistedProduct(Product product) {
        return productRepository.findById(product.getId()).orElseThrow();
    }

    protected void assertPersistedProductToMatchAllProperties(Product expectedProduct) {
        assertProductAllPropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }

    protected void assertPersistedProductToMatchUpdatableProperties(Product expectedProduct) {
        assertProductAllUpdatablePropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }
}
