package com.supermatech.domain;

import static com.supermatech.domain.OrderLineTestSamples.*;
import static com.supermatech.domain.OrderTestSamples.*;
import static com.supermatech.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.supermatech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderLine.class);
        OrderLine orderLine1 = getOrderLineSample1();
        OrderLine orderLine2 = new OrderLine();
        assertThat(orderLine1).isNotEqualTo(orderLine2);

        orderLine2.setId(orderLine1.getId());
        assertThat(orderLine1).isEqualTo(orderLine2);

        orderLine2 = getOrderLineSample2();
        assertThat(orderLine1).isNotEqualTo(orderLine2);
    }

    @Test
    void productTest() {
        OrderLine orderLine = getOrderLineRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        orderLine.setProduct(productBack);
        assertThat(orderLine.getProduct()).isEqualTo(productBack);

        orderLine.product(null);
        assertThat(orderLine.getProduct()).isNull();
    }

    @Test
    void orderTest() {
        OrderLine orderLine = getOrderLineRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderLine.setOrder(orderBack);
        assertThat(orderLine.getOrder()).isEqualTo(orderBack);

        orderLine.order(null);
        assertThat(orderLine.getOrder()).isNull();
    }
}
