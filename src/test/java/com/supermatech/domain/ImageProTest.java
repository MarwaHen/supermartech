package com.supermatech.domain;

import static com.supermatech.domain.ImageProTestSamples.*;
import static com.supermatech.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.supermatech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageProTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImagePro.class);
        ImagePro imagePro1 = getImageProSample1();
        ImagePro imagePro2 = new ImagePro();
        assertThat(imagePro1).isNotEqualTo(imagePro2);

        imagePro2.setId(imagePro1.getId());
        assertThat(imagePro1).isEqualTo(imagePro2);

        imagePro2 = getImageProSample2();
        assertThat(imagePro1).isNotEqualTo(imagePro2);
    }

    @Test
    void productTest() {
        ImagePro imagePro = getImageProRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        imagePro.setProduct(productBack);
        assertThat(imagePro.getProduct()).isEqualTo(productBack);

        imagePro.product(null);
        assertThat(imagePro.getProduct()).isNull();
    }
}
