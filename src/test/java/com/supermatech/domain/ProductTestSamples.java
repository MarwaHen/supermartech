package com.supermatech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .pro_name("pro_name1")
            .pro_description("pro_description1")
            .pro_quantity(1)
            .catt_id(1)
            .pro_promotion(1)
            .pro_mark("pro_mark1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .pro_name("pro_name2")
            .pro_description("pro_description2")
            .pro_quantity(2)
            .catt_id(2)
            .pro_promotion(2)
            .pro_mark("pro_mark2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .pro_name(UUID.randomUUID().toString())
            .pro_description(UUID.randomUUID().toString())
            .pro_quantity(intCount.incrementAndGet())
            .catt_id(intCount.incrementAndGet())
            .pro_promotion(intCount.incrementAndGet())
            .pro_mark(UUID.randomUUID().toString());
    }
}
