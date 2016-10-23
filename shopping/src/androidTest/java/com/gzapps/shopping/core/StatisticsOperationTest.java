/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class StatisticsOperationTest extends StatisticsTest {

    public void testKey0() {
        assertEquals(0x00007fff, Statistics.key((short) 0x7fff, (short) 0));
    }

    public void testKey1() {
        assertEquals(0x00007fff, Statistics.key((short) 0, (short) 0x7fff));
    }

    public void testKey2() {
        assertEquals(0xd0d05e5e,
                Statistics.key((short) 0xd0d0, (short) 0x5e5e));
    }

    public void testKey3() {
        assertEquals(0xffff7fff,
                Statistics.key((short) 0x7fff, (short) 0xffff));
    }

    public void testCorrelation0() {
        // when
        float correlation = statistics.correlation(product1, product1);
        // then
        assertEquals(1F, correlation, DELTA);
    }

    public void testDistance0() {
        // when
        float distance = statistics.distance(product1, product1);
        // then
        assertEquals(0, distance, DELTA);
    }

    public void testCorrelation1() {
        // when
        float correlation = statistics.correlation(product1, product2);
        // then
        assertEquals(0F, correlation, DELTA);
    }

    public void testDistance1() {
        // when
        float distance = statistics.distance(product1, product2);
        // then
        assertEquals(Statistics.DISTANCE_DEFAULT, distance, DELTA);
    }

    public void testCorrelation2() {
        // when
        statistics.correlation(product1, product2, 0.4543F);
        float correlation = statistics.correlation(product1, product2);
        // then
        assertEquals(0.4543F, correlation, DELTA);
    }

    public void testDistance2() {
        // when
        statistics.distance(product1, product2, 4543);
        int distance = statistics.distance(product1, product2);
        // then
        assertEquals(4543, distance);
    }

    public void testCorrelation3() {
        // when
        statistics.correlation(product2, product1, 0.4543F);
        float correlation = statistics.correlation(product1, product2);
        // then
        assertEquals(0.4543F, correlation, DELTA);
    }

    public void testDistance3() {
        // when
        statistics.distance(product2, product1, 4543);
        int distance = statistics.distance(product1, product2);
        // then
        assertEquals(4543, distance);
    }

    public void testCorrelation4() {
        // when
        statistics.correlation(product1, product2, 0.4543F);
        float correlation = statistics.correlation(product2, product1);
        // then
        assertEquals(0.4543F, correlation, DELTA);
    }

    public void testDistance4() {
        // when
        statistics.distance(product1, product2, 4543);
        int distance = statistics.distance(product2, product1);
        // then
        assertEquals(4543, distance);
    }

    public void testCorrelation5() {
        // when
        statistics.correlation(product2, product1, 0.4543F);
        float correlation = statistics.correlation(product2, product1);
        // then
        assertEquals(0.4543F, correlation, DELTA);
    }

    public void testDistance5() {
        // when
        statistics.distance(product2, product1, 4543);
        int distance = statistics.distance(product2, product1);
        // then
        assertEquals(4543, distance);
    }

    public void testCorrelation6() {
        // when
        statistics.correlation(product2, product1, 0.4543F);
        float correlation = statistics.correlation(product1, product3);
        // then
        assertEquals(0F, correlation, DELTA);
    }

    public void testDistance6() {
        // when
        statistics.distance(product2, product1, 4543);
        int distance = statistics.distance(product1, product3);
        // then
        assertEquals(Statistics.DISTANCE_DEFAULT, distance);
    }

    public void testCorrelation7() {
        // when
        statistics.correlation(product2, product1, 0.4543F);
        float correlation = statistics.correlation(product3, product1);
        // then
        assertEquals(0F, correlation, DELTA);
    }

    public void testDistance7() {
        // when
        statistics.distance(product2, product1, 4543);
        int distance = statistics.distance(product3, product1);
        // then
        assertEquals(Statistics.DISTANCE_DEFAULT, distance);
    }

    public void testResize() {
        // given
        Product[] products = new Product[100];
        for (int i = 0; i < products.length; ++i) {
            products[i] = shopping.create(String.valueOf(i));
        }
        // when
        for (int i = 0; i < products.length; ++i) {
            for (int j = i + 1; j < products.length; ++j) {
                statistics.correlation(products[i], products[j],
                        (float) ((i + 1.0) / (j + 1.0)));
                statistics.distance(products[i], products[j], i * j);
            }
        }
        // then
        for (int i = 0; i < products.length; ++i) {
            for (int j = i + 1; j < products.length; ++j) {
                float correlation =
                        statistics.correlation(products[i], products[j]);
                int distance = statistics.distance(products[i], products[j]);
                assertEquals((float) ((i + 1.0) / (j + 1.0)), correlation,
                        DELTA);
                assertEquals(i * j, distance);
            }
        }
    }
}
