/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping;

import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.gzapps.shopping.app.ShoppingApplication;
import com.gzapps.shopping.core.Analysis;
import com.gzapps.shopping.core.Shopping;

import java.io.IOException;

import static com.gzapps.shopping.TimeValues.TODAY;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
@LargeTest
public class AnalyzeTest extends ApplicationTestCase<ShoppingApplication> {

    private ShoppingApplication application;

    public AnalyzeTest() {
        super(ShoppingApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();
    }

    public void test() throws IOException {
        application.acquire();
        try {
            application.load();
            Shopping shopping = application.shopping();
            if (shopping == null) {
                return;
            }

            Analysis analysis = new Analysis(shopping, TODAY);
            analysis.analyze();
            application.save();
        } finally {
            application.release();
        }
    }
}
