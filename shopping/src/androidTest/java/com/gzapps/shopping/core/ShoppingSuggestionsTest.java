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
import static com.gzapps.shopping.TimeValues.THREE_WEEKS_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.TWO_WEEKS_AGO;

public class ShoppingSuggestionsTest extends ShoppingTest {

    private static final float IMPORTANCE = 0.5f;

    private Product product1;

    private Product product2;

    private Product product3;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product1 = shopping.create("product1");
        product2 = shopping.create("product2");
        product3 = shopping.create("product3");
    }

    public void testEnlisted() {
        // given
        product1.enlist();
        // when
        List<Product> suggestions = shopping.suggestions(TODAY, IMPORTANCE, 1);
        // then
        assertEquals(0, suggestions.size());
    }

    public void testCorrelated() {
        // given
        product1.buy(SIX_DAYS_AGO);
        product1.buy(FIVE_DAYS_AGO);
        product1.buy(FOUR_DAYS_AGO);
        product1.buy(THREE_DAYS_AGO);
        product1.buy(TWO_DAYS_AGO);

        product2.buy(FIVE_DAYS_AGO);
        product2.buy(FOUR_DAYS_AGO);
        product2.buy(THREE_DAYS_AGO);
        product2.buy(TWO_DAYS_AGO);
        product2.buy(ONE_DAY_AGO);

        product3.buy(THREE_WEEKS_AGO);
        product3.buy(TWO_WEEKS_AGO);
        product3.buy(SEVEN_DAYS_AGO);
        product3.buy(SIX_DAYS_AGO);
        product3.buy(FOUR_DAYS_AGO);
        product3.buy(THREE_DAYS_AGO);
        product3.buy(TWO_DAYS_AGO);
        product3.buy(ONE_DAY_AGO);

        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();

        product1.enlist();

        // when
        List<Product> suggestions = shopping.suggestions(TODAY, IMPORTANCE, 1);

        // then
        assertEquals(1, suggestions.size());
        Product suggested = suggestions.get(0);
        assertEquals(product2, suggested);
    }

    public void testNotCorrelated() {
        // given
        product1.buy(SIX_DAYS_AGO);
        product1.buy(FIVE_DAYS_AGO);
        product1.buy(FOUR_DAYS_AGO);
        product1.buy(THREE_DAYS_AGO);

        product2.buy(THREE_DAYS_AGO);
        product2.buy(TWO_DAYS_AGO);
        product2.buy(ONE_DAY_AGO);

        product3.buy(FIVE_DAYS_AGO);
        product3.buy(ONE_DAY_AGO);

        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();
        product1.enlist();

        // when
        List<Product> suggestions = shopping.suggestions(TODAY, IMPORTANCE, 1);

        // then
        assertEquals(0, suggestions.size());
    }

    public void testImportances() {
        // given
        product1.buy(SIX_DAYS_AGO);
        product1.buy(FIVE_DAYS_AGO);
        product1.buy(FOUR_DAYS_AGO);
        product1.buy(THREE_DAYS_AGO);
        product1.buy(TWO_DAYS_AGO);

        product2.buy(SIX_DAYS_AGO);
        product2.buy(FIVE_DAYS_AGO);
        product2.buy(FOUR_DAYS_AGO);
        product2.buy(THREE_DAYS_AGO);

        product3.buy(SEVEN_DAYS_AGO);
        product3.buy(SIX_DAYS_AGO);
        product3.buy(FOUR_DAYS_AGO);
        product3.buy(THREE_DAYS_AGO);
        product3.buy(TWO_DAYS_AGO);
        product3.buy(ONE_DAY_AGO);

        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();

        product1.enlist();

        // when
        List<Product> suggestions = shopping.suggestions(TODAY, IMPORTANCE, 1);

        // then
        assertEquals(1, suggestions.size());

        Product suggested1 = suggestions.get(0);
        assertEquals(product2, suggested1);
    }

    public void testLimit() {
        // given
        product1.buy(SIX_DAYS_AGO);
        product1.buy(FIVE_DAYS_AGO);
        product1.buy(FOUR_DAYS_AGO);
        product1.buy(THREE_DAYS_AGO);

        product2.buy(FIVE_DAYS_AGO);
        product2.buy(FOUR_DAYS_AGO);
        product2.buy(THREE_DAYS_AGO);
        product2.buy(TWO_DAYS_AGO);

        product3.buy(SIX_DAYS_AGO);
        product3.buy(TWO_DAYS_AGO);

        Analysis analysis = new Analysis(shopping, TODAY);
        analysis.analyze();
        product1.enlist();

        // when
        List<Product> suggestions =
                shopping.suggestions(TODAY, IMPORTANCE, -0.00001F);

        // then
        assertEquals(0, suggestions.size());
    }
}
