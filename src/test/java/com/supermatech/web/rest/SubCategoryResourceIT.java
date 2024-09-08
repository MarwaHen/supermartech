package com.supermatech.web.rest;

import static com.supermatech.domain.SubCategoryAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermatech.IntegrationTest;
import com.supermatech.domain.Category;
import com.supermatech.domain.SubCategory;
import com.supermatech.repository.SubCategoryRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubCategoryResourceIT {

    private static final String DEFAULT_CATT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CATT_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAT_ID = 1;
    private static final Integer UPDATED_CAT_ID = 2;

    private static final String DEFAULT_CATT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_CATT_ICON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sub-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubCategoryMockMvc;

    private SubCategory subCategory;

    private SubCategory insertedSubCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createEntity(EntityManager em) {
        SubCategory subCategory = new SubCategory()
            .catt_name(DEFAULT_CATT_NAME)
            .catt_description(DEFAULT_CATT_DESCRIPTION)
            .cat_id(DEFAULT_CAT_ID)
            .catt_icon(DEFAULT_CATT_ICON);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity();
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        subCategory.setCategory(category);
        return subCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createUpdatedEntity(EntityManager em) {
        SubCategory updatedSubCategory = new SubCategory()
            .catt_name(UPDATED_CATT_NAME)
            .catt_description(UPDATED_CATT_DESCRIPTION)
            .cat_id(UPDATED_CAT_ID)
            .catt_icon(UPDATED_CATT_ICON);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity();
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        updatedSubCategory.setCategory(category);
        return updatedSubCategory;
    }

    @BeforeEach
    public void initTest() {
        subCategory = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubCategory != null) {
            subCategoryRepository.delete(insertedSubCategory);
            insertedSubCategory = null;
        }
    }

    @Test
    @Transactional
    void getAllSubCategories() throws Exception {
        // Initialize the database
        insertedSubCategory = subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].catt_name").value(hasItem(DEFAULT_CATT_NAME)))
            .andExpect(jsonPath("$.[*].catt_description").value(hasItem(DEFAULT_CATT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cat_id").value(hasItem(DEFAULT_CAT_ID)))
            .andExpect(jsonPath("$.[*].catt_icon").value(hasItem(DEFAULT_CATT_ICON)));
    }

    @Test
    @Transactional
    void getSubCategory() throws Exception {
        // Initialize the database
        insertedSubCategory = subCategoryRepository.saveAndFlush(subCategory);

        // Get the subCategory
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, subCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subCategory.getId().intValue()))
            .andExpect(jsonPath("$.catt_name").value(DEFAULT_CATT_NAME))
            .andExpect(jsonPath("$.catt_description").value(DEFAULT_CATT_DESCRIPTION))
            .andExpect(jsonPath("$.cat_id").value(DEFAULT_CAT_ID))
            .andExpect(jsonPath("$.catt_icon").value(DEFAULT_CATT_ICON));
    }

    @Test
    @Transactional
    void getNonExistingSubCategory() throws Exception {
        // Get the subCategory
        restSubCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    protected long getRepositoryCount() {
        return subCategoryRepository.count();
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

    protected SubCategory getPersistedSubCategory(SubCategory subCategory) {
        return subCategoryRepository.findById(subCategory.getId()).orElseThrow();
    }

    protected void assertPersistedSubCategoryToMatchAllProperties(SubCategory expectedSubCategory) {
        assertSubCategoryAllPropertiesEquals(expectedSubCategory, getPersistedSubCategory(expectedSubCategory));
    }

    protected void assertPersistedSubCategoryToMatchUpdatableProperties(SubCategory expectedSubCategory) {
        assertSubCategoryAllUpdatablePropertiesEquals(expectedSubCategory, getPersistedSubCategory(expectedSubCategory));
    }
}
