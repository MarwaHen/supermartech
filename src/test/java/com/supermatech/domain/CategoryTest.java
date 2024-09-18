package com.supermatech.domain;

import static com.supermatech.domain.CategoryTestSamples.*;
import static com.supermatech.domain.SubCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.supermatech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void subCategoryTest() {
        Category category = getCategoryRandomSampleGenerator();
        SubCategory subCategoryBack = getSubCategoryRandomSampleGenerator();

        category.addSubCategory(subCategoryBack);
        assertThat(category.getSubCategories()).containsOnly(subCategoryBack);
        assertThat(subCategoryBack.getCategory()).isEqualTo(category);

        category.removeSubCategory(subCategoryBack);
        assertThat(category.getSubCategories()).doesNotContain(subCategoryBack);
        assertThat(subCategoryBack.getCategory()).isNull();

        category.subCategories(new HashSet<>(Set.of(subCategoryBack)));
        assertThat(category.getSubCategories()).containsOnly(subCategoryBack);
        assertThat(subCategoryBack.getCategory()).isEqualTo(category);

        category.setSubCategories(new HashSet<>());
        assertThat(category.getSubCategories()).doesNotContain(subCategoryBack);
        assertThat(subCategoryBack.getCategory()).isNull();
    }
}
