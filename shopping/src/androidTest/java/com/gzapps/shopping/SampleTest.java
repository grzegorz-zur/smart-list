/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping;

import android.content.res.Resources;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.gzapps.shopping.app.ShoppingApplication;
import com.gzapps.shopping.core.Analysis;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.io.IOException;
import java.math.BigDecimal;

import static com.gzapps.shopping.TimeValues.FIVE_WEEKS_AGO;
import static com.gzapps.shopping.TimeValues.FOUR_WEEKS_AGO;
import static com.gzapps.shopping.TimeValues.ONE_WEEK_AGO;
import static com.gzapps.shopping.TimeValues.THREE_WEEKS_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_WEEKS_AGO;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
@MediumTest
public class SampleTest extends ApplicationTestCase<ShoppingApplication> {

    private ShoppingApplication application;

    private Shopping shopping;

    public SampleTest() {
        super(ShoppingApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();
    }

    @SuppressWarnings("ConstantConditions")
    public void test() throws IOException {
        application.acquire();
        try {
            application.create();
            shopping = application.shopping();
            shopping.clearProducts();

            products();

            // five weeks ago
            product("eggs").enlist();
            product("bread").enlist();
            product("corn").enlist();
            product("pork").enlist();
            product("water").enlist();
            product("lettuce").enlist();

            shopping.buyAll(FIVE_WEEKS_AGO);

            // four weeks ago
            product("bread").enlist();
            product("milk").enlist();
            product("cereal").enlist();
            product("pepper").enlist();
            product("muffins").enlist();
            product("apples").enlist();
            product("rice").enlist();
            product("soda").enlist();

            shopping.buyAll(FOUR_WEEKS_AGO);

            // three weeks ago
            product("bread").enlist();
            product("milk").enlist();
            product("sausages").enlist();
            product("coffee").enlist();
            product("pork").enlist();
            product("jam").enlist();
            product("apples").enlist();
            product("chicken").enlist();
            product("corn").enlist();

            shopping.buyAll(THREE_WEEKS_AGO);

            // two weeks ago
            product("butter").enlist();
            product("bread").enlist();
            product("cheese").enlist();
            product("milk").enlist();
            product("pork").enlist();
            product("apples").enlist();
            product("beans").enlist();
            product("pepper").enlist();
            product("sausages").enlist();
            product("rice").enlist();

            shopping.buyAll(TWO_WEEKS_AGO);

            // one week ago
            product("fish").enlist();
            product("bread").enlist();
            product("coffee").enlist();
            product("muffins").enlist();
            product("rice").enlist();
            product("eggs").enlist();
            product("jam").enlist();
            product("water").enlist();
            product("cereal").enlist();
            product("pepper").enlist();
            product("carrots").enlist();
            product("sausages").enlist();
            product("lettuce").enlist();
            product("cheese").enlist();

            shopping.buyAll(ONE_WEEK_AGO);

            // current list
            product("bread").enlist();
            product("milk").quantitize(BigDecimal.ONE, "l");
            product("milk").enlist();
            product("soda").quantitize(BigDecimal.valueOf(6), "cans");
            product("soda").enlist();
            product("ice cream").enlist();
            product("butter").enlist();
            product("jam").quantitize(BigDecimal.ONE, "");
            product("jam").enlist();
            product("sausages").enlist();
            product("chicken").enlist();
            product("rice").quantitize(BigDecimal.valueOf(2), "packs");
            product("rice").enlist();
            product("pasta").enlist();
            product("coffee").enlist();

            // analysis
            Analysis analysis = new Analysis(shopping, TODAY);
            analysis.analyze();
            application.save();
        } finally {
            application.release();
        }
    }

    private void products() {
        Resources resources = application.getResources();
        String[] names = resources.getStringArray(R.array.sample_products);
        for (String name : names) {
            shopping.create(name);
        }
    }

    private Product product(String name) {
        Product product = shopping.find(name);
        if (product == null) {
            product = shopping.create(name);
        }
        return product;
    }
}
