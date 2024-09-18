package com.supermatech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Category getCategorySample1() {
        return new Category().id(1L).cat_name("cat_name1").cat_description("cat_description1");
    }

    public static Category getCategorySample2() {
        return new Category().id(2L).cat_name("cat_name2").cat_description("cat_description2");
    }

    public static Category getCategoryRandomSampleGenerator() {
        return new Category()
            .id(longCount.incrementAndGet())
            .cat_name(UUID.randomUUID().toString())
            .cat_description(UUID.randomUUID().toString());
    }
}
