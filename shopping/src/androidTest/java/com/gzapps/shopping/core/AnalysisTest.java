/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import junit.framework.TestCase;

import static com.gzapps.shopping.TimeValues.TODAY;

abstract class AnalysisTest extends TestCase {

    protected Shopping shopping;

    protected Analysis analysis;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        shopping = new Shopping();
        analysis = new Analysis(shopping, TODAY, true);
    }
}
