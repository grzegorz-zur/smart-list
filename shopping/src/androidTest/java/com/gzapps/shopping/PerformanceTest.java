/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping;

import android.os.Debug;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.gzapps.shopping.app.ShoppingApplication;
import com.gzapps.shopping.core.Analysis;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.io.IOException;
import java.util.Random;

import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.core.Time.DAY;

@SuppressWarnings(
        {"JUnitTestMethodWithNoAssertions", "JUnitTestCaseWithNoTests"})
@LargeTest
public class PerformanceTest extends ApplicationTestCase<ShoppingApplication> {

    private static final int PRODUCTS = 100;
    private static final int DAYS = 90;
    private static final int INTERVAL = 3;
    private final Random random = new Random();
    private ShoppingApplication application;
    private Shopping shopping;

    public PerformanceTest() {
        super(ShoppingApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();
    }

    @SuppressWarnings("InstanceMethodNamingConvention")
    public void _testAnalysis() {
        application.acquire();
        try {
            populate();
            Debug.startMethodTracing("analysis");
            Analysis analysis = new Analysis(shopping, TODAY);
            analysis.analyze();
            Debug.stopMethodTracing();
        } finally {
            application.release();
        }
    }

    @SuppressWarnings("InstanceMethodNamingConvention")
    public void _testSaveLoad() throws IOException {
        application.acquire();
        try {
            populate();

            Debug.startMethodTracing("save");
            application.save();
            Debug.stopMethodTracing();

            Debug.startMethodTracing("load");
            application.load();
            Debug.stopMethodTracing();
        } finally {
            application.release();
        }
    }

    private void populate() {
        application.create();
        shopping = application.shopping();
        shopping.clearProducts();

        for (int i = 0; i < PRODUCTS; ++i) {
            shopping.create("product " + i);
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
    }
}
