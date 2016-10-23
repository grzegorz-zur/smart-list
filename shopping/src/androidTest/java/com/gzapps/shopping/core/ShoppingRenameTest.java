/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ShoppingRenameTest extends ShoppingTest {

    public void testRename() {
        // given
        Product product = shopping.create("product");
        // when
        product.rename("name");
        // then
        Product foundProduct = shopping.find("name");
        assertEquals(product, foundProduct);
    }
}
