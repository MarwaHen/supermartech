package com.supermatech.domain;

import static com.supermatech.domain.ProductTestSamples.*;
import static com.supermatech.domain.SubCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.supermatech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void subCategoryTest() {
        Product product = getProductRandomSampleGenerator();
        SubCategory subCategoryBack = getSubCategoryRandomSampleGenerator();

        product.setSubCategory(subCategoryBack);
        assertThat(product.getSubCategory()).isEqualTo(subCategoryBack);

        product.subCategory(null);
        assertThat(product.getSubCategory()).isNull();
    }
}
