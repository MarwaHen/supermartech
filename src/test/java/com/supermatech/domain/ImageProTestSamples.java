package com.supermatech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ImageProTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ImagePro getImageProSample1() {
        return new ImagePro().id(1L).imgP_Path("imgP_Path1").pro_id(1);
    }

    public static ImagePro getImageProSample2() {
        return new ImagePro().id(2L).imgP_Path("imgP_Path2").pro_id(2);
    }

    public static ImagePro getImageProRandomSampleGenerator() {
        return new ImagePro().id(longCount.incrementAndGet()).imgP_Path(UUID.randomUUID().toString()).pro_id(intCount.incrementAndGet());
    }
}
