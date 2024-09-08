package com.supermatech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubCategory getSubCategorySample1() {
        return new SubCategory().id(1L).catt_name("catt_name1").catt_description("catt_description1").cat_id(1).catt_icon("catt_icon1");
    }

    public static SubCategory getSubCategorySample2() {
        return new SubCategory().id(2L).catt_name("catt_name2").catt_description("catt_description2").cat_id(2).catt_icon("catt_icon2");
    }

    public static SubCategory getSubCategoryRandomSampleGenerator() {
        return new SubCategory()
            .id(longCount.incrementAndGet())
            .catt_name(UUID.randomUUID().toString())
            .catt_description(UUID.randomUUID().toString())
            .cat_id(intCount.incrementAndGet())
            .catt_icon(UUID.randomUUID().toString());
    }
}
