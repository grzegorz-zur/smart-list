/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ShoppingExistsTest extends ShoppingTest {

    public void testNotExists() {
        // when
        boolean exists = shopping.exists("name");
        // then
        assertFalse(exists);
    }

    public void testExists() {
        // given
        shopping.create("name");
        // when
        boolean exists = shopping.exists("name");
        // then
        assertTrue(exists);
    }
}
