/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping;

import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.gzapps.shopping.app.ShoppingApplication;
import com.gzapps.shopping.core.Analysis;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.io.IOException;
import java.util.Random;

import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.app.ShoppingApplication.SIZE;
import static com.gzapps.shopping.core.Analysis.LIMIT;
import static com.gzapps.shopping.core.Time.DAY;

@SuppressWarnings(
        {"JUnitTestMethodWithNoAssertions", "JUnitTestCaseWithNoTests"})
@LargeTest
public class FillingTest extends ApplicationTestCase<ShoppingApplication> {

    private static final int PRODUCTS = SIZE;
    private static final int DAYS = (int) (LIMIT / DAY);
    private static final int INTERVAL = 1;
    private final Random random = new Random();
    private ShoppingApplication application;

    public FillingTest() {
        super(ShoppingApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();
    }

    @SuppressWarnings("InstanceMethodNamingConvention")
    public void _test() throws IOException {
        application.acquire();
        try {
            application.create();
            Shopping shopping = application.shopping();
            shopping.clearProducts();

            for (int i = 0; i < PRODUCTS; ++i) {
                shopping.create(String.format("product %03d", i));
            }

            long now = TODAY;
            for (int day = DAYS; day > 0; day -= INTERVAL) {
                long date = now - day * DAY;
                for (Product product : shopping.products()) {
                    if (random.nextBoolean()) {
                        product.buy(date);
                    }
                }
            }

            for (Product product : shopping.products()) {
                if (random.nextBoolean()) {
                    product.enlist();
                }
            }

            Analysis analysis = new Analysis(shopping, TODAY);
            analysis.analyze();

            application.save();
        } finally {
            application.release();
        }
    }
}
