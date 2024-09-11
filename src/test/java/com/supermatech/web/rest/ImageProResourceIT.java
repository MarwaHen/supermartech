package com.supermatech.web.rest;

import static com.supermatech.domain.ImageProAsserts.*;
import static com.supermatech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermatech.IntegrationTest;
import com.supermatech.domain.ImagePro;
import com.supermatech.domain.Product;
import com.supermatech.repository.ImageProRepository;
import com.supermatech.service.ImageProService;
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
 * Integration tests for the {@link ImageProResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ImageProResourceIT {

    private static final String DEFAULT_IMG_P_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMG_P_PATH = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRO_ID = 1;
    private static final Integer UPDATED_PRO_ID = 2;

    private static final String ENTITY_API_URL = "/api/image-pros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageProRepository imageProRepository;

    @Mock
    private ImageProRepository imageProRepositoryMock;

    @Mock
    private ImageProService imageProServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageProMockMvc;

    private ImagePro imagePro;

    private ImagePro insertedImagePro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagePro createEntity(EntityManager em) {
        ImagePro imagePro = new ImagePro().imgP_Path(DEFAULT_IMG_P_PATH).pro_id(DEFAULT_PRO_ID);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        imagePro.setProduct(product);
        return imagePro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagePro createUpdatedEntity(EntityManager em) {
        ImagePro updatedImagePro = new ImagePro().imgP_Path(UPDATED_IMG_P_PATH).pro_id(UPDATED_PRO_ID);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedImagePro.setProduct(product);
        return updatedImagePro;
    }

    @BeforeEach
    public void initTest() {
        imagePro = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedImagePro != null) {
            imageProRepository.delete(insertedImagePro);
            insertedImagePro = null;
        }
    }

    @Test
    @Transactional
    void createImagePro() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ImagePro
        var returnedImagePro = om.readValue(
            restImageProMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePro)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImagePro.class
        );

        // Validate the ImagePro in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertImageProUpdatableFieldsEquals(returnedImagePro, getPersistedImagePro(returnedImagePro));

        insertedImagePro = returnedImagePro;
    }

    @Test
    @Transactional
    void createImageProWithExistingId() throws Exception {
        // Create the ImagePro with an existing ID
        imagePro.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageProMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePro)))
            .andExpect(status().isBadRequest());

        // Validate the ImagePro in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkImgP_PathIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        imagePro.setImgP_Path(null);

        // Create the ImagePro, which fails.

        restImageProMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePro)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPro_idIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        imagePro.setPro_id(null);

        // Create the ImagePro, which fails.

        restImageProMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePro)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImagePros() throws Exception {
        // Initialize the database
        insertedImagePro = imageProRepository.saveAndFlush(imagePro);

        // Get all the imageProList
        restImageProMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagePro.getId().intValue())))
            .andExpect(jsonPath("$.[*].imgP_Path").value(hasItem(DEFAULT_IMG_P_PATH)))
            .andExpect(jsonPath("$.[*].pro_id").value(hasItem(DEFAULT_PRO_ID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImageProsWithEagerRelationshipsIsEnabled() throws Exception {
        when(imageProServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImageProMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(imageProServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImageProsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(imageProServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImageProMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(imageProRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getImagePro() throws Exception {
        // Initialize the database
        insertedImagePro = imageProRepository.saveAndFlush(imagePro);

        // Get the imagePro
        restImageProMockMvc
            .perform(get(ENTITY_API_URL_ID, imagePro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imagePro.getId().intValue()))
            .andExpect(jsonPath("$.imgP_Path").value(DEFAULT_IMG_P_PATH))
            .andExpect(jsonPath("$.pro_id").value(DEFAULT_PRO_ID));
    }

    @Test
    @Transactional
    void getNonExistingImagePro() throws Exception {
        // Get the imagePro
        restImageProMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImagePro() throws Exception {
        // Initialize the database
        insertedImagePro = imageProRepository.saveAndFlush(imagePro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imagePro
        ImagePro updatedImagePro = imageProRepository.findById(imagePro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImagePro are not directly saved in db
        em.detach(updatedImagePro);
        updatedImagePro.imgP_Path(UPDATED_IMG_P_PATH).pro_id(UPDATED_PRO_ID);

        restImageProMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImagePro.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedImagePro))
            )
            .andExpect(status().isOk());

        // Validate the ImagePro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageProToMatchAllProperties(updatedImagePro);
    }

    @Test
    @Transactional
    void putNonExistingImagePro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imagePro.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageProMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagePro.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePro))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagePro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImagePro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imagePro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageProMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imagePro))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagePro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImagePro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imagePro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageProMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImagePro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageProWithPatch() throws Exception {
        // Initialize the database
        insertedImagePro = imageProRepository.saveAndFlush(imagePro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imagePro using partial update
        ImagePro partialUpdatedImagePro = new ImagePro();
        partialUpdatedImagePro.setId(imagePro.getId());

        partialUpdatedImagePro.imgP_Path(UPDATED_IMG_P_PATH).pro_id(UPDATED_PRO_ID);

        restImageProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagePro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImagePro))
            )
            .andExpect(status().isOk());

        // Validate the ImagePro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageProUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedImagePro, imagePro), getPersistedImagePro(imagePro));
    }

    @Test
    @Transactional
    void fullUpdateImageProWithPatch() throws Exception {
        // Initialize the database
        insertedImagePro = imageProRepository.saveAndFlush(imagePro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imagePro using partial update
        ImagePro partialUpdatedImagePro = new ImagePro();
        partialUpdatedImagePro.setId(imagePro.getId());

        partialUpdatedImagePro.imgP_Path(UPDATED_IMG_P_PATH).pro_id(UPDATED_PRO_ID);

        restImageProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagePro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImagePro))
            )
            .andExpect(status().isOk());

        // Validate the ImagePro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageProUpdatableFieldsEquals(partialUpdatedImagePro, getPersistedImagePro(partialUpdatedImagePro));
    }

    @Test
    @Transactional
    void patchNonExistingImagePro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imagePro.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imagePro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imagePro))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagePro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImagePro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imagePro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageProMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imagePro))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagePro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImagePro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        imagePro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageProMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(imagePro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImagePro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImagePro() throws Exception {
        // Initialize the database
        insertedImagePro = imageProRepository.saveAndFlush(imagePro);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the imagePro
        restImageProMockMvc
            .perform(delete(ENTITY_API_URL_ID, imagePro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return imageProRepository.count();
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

    protected ImagePro getPersistedImagePro(ImagePro imagePro) {
        return imageProRepository.findById(imagePro.getId()).orElseThrow();
    }

    protected void assertPersistedImageProToMatchAllProperties(ImagePro expectedImagePro) {
        assertImageProAllPropertiesEquals(expectedImagePro, getPersistedImagePro(expectedImagePro));
    }

    protected void assertPersistedImageProToMatchUpdatableProperties(ImagePro expectedImagePro) {
        assertImageProAllUpdatablePropertiesEquals(expectedImagePro, getPersistedImagePro(expectedImagePro));
    }
}
