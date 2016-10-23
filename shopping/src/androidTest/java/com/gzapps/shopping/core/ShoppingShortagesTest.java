/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import java.util.List;

import static com.gzapps.shopping.TimeValues.FIVE_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.FOUR_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.SEVEN_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.SIX_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.THREE_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;

public class ShoppingShortagesTest extends ShoppingTest {

    public void testEnlisted() {
        // given
        Product product = shopping.create("product");
        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();
        product.enlist();
        // when
        List<Product> shortages = shopping.shortages(TODAY, -1, 1);
        // then
        assertEquals(0, shortages.size());
    }

    public void testBounds0() {
        // given
        Product product = shopping.create("product");
        product.buy(FIVE_DAYS_AGO);
        product.buy(TWO_DAYS_AGO);
        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();
        // when
        List<Product> shortages = shopping.shortages(TODAY, 0, 0.4F);
        // then
        assertEquals(1, shortages.size());
        Product suggested = shortages.get(0);
        assertEquals(product, suggested);
    }

    public void testBounds1() {
        // given
        Product product = shopping.create("product");
        product.buy(SIX_DAYS_AGO);
        product.buy(TODAY);
        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();
        // when
        List<Product> shortages = shopping.shortages(TODAY, -1, 0.5F);
        // then
        assertEquals(0, shortages.size());
    }

    public void testBounds2() {
        // given
        Product product = shopping.create("product");
        product.buy(SIX_DAYS_AGO);
        product.buy(FOUR_DAYS_AGO);
        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();
        // when
        List<Product> shortages = shopping.shortages(TODAY, -2.5F, -1.5F);
        // then
        assertEquals(0, shortages.size());
    }

    public void testNotBought0() {
        // given
        Product product = shopping.create("product");
        product.buy(SEVEN_DAYS_AGO);
        product.buy(SIX_DAYS_AGO);
        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();
        // when
        List<Product> shortages = shopping.shortages(TODAY, -1, 0.5F);
        // then
        assertEquals(0, shortages.size());
    }

    public void testNotBought1() {
        // given
        Product product = shopping.create("product");
        product.buy(SEVEN_DAYS_AGO);
        product.buy(ONE_DAY_AGO);
        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();
        // when
        List<Product> shortages = shopping.shortages(TODAY, 0, 5F / 6F);
        // then
        assertEquals(1, shortages.size());
    }

    public void testNotBought2() {
        // given
        Product product = shopping.create("product");
        product.buy(THREE_DAYS_AGO);
        product.buy(TWO_DAYS_AGO);
        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();
        // when
        List<Product> shortages = shopping.shortages(TODAY, -1, 1);
        // then
        assertEquals(1, shortages.size());
    }

    public void testImportances() {
        // given
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        Product product3 = shopping.create("product3");

        product1.buy(SIX_DAYS_AGO);
        product1.buy(FIVE_DAYS_AGO);
        product1.buy(FOUR_DAYS_AGO);
        product1.buy(THREE_DAYS_AGO);

        product2.buy(FIVE_DAYS_AGO);
        product2.buy(FOUR_DAYS_AGO);
        product2.buy(THREE_DAYS_AGO);
        product2.buy(TWO_DAYS_AGO);

        product3.buy(TWO_DAYS_AGO);
        product3.buy(ONE_DAY_AGO);

        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();

        // when
        List<Product> shortages = shopping.shortages(TODAY, -2, 1);

        // then
        assertEquals(3, shortages.size());

        Product shortage1 = shortages.get(0);
        assertEquals(product1, shortage1);

        Product shortage2 = shortages.get(1);
        assertEquals(product2, shortage2);

        Product shortage3 = shortages.get(2);
        assertEquals(product3, shortage3);
    }
}
