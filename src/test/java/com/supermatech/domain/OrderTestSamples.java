package com.supermatech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Order getOrderSample1() {
        return new Order().id(1L).odr_adresse("odr_adresse1").odr_phonenumber("odr_phonenumber1");
    }

    public static Order getOrderSample2() {
        return new Order().id(2L).odr_adresse("odr_adresse2").odr_phonenumber("odr_phonenumber2");
    }

    public static Order getOrderRandomSampleGenerator() {
        return new Order()
            .id(longCount.incrementAndGet())
            .odr_adresse(UUID.randomUUID().toString())
            .odr_phonenumber(UUID.randomUUID().toString());
    }
}
