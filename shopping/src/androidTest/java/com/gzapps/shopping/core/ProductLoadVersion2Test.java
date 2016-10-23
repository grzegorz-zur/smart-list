/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import org.apache.commons.collections.primitives.LongList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;

@SmallTest
public class ProductLoadVersion2Test extends ProductTest {

    private InputOutput io;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        io = new InputOutput();
    }

    public void testLoad() throws IOException {
        // given
        DataOutputStream output = io.output();
        output.writeShort(7);
        output.writeUTF("product");
        output.writeBoolean(true);
        output.writeUTF("10.01");
        output.writeUTF("kg");
        output.writeInt(2);
        output.writeLong(ONE_DAY_AGO);
        output.writeLong(TODAY);
        DataInputStream input = io.input();
        // when
        Product product = new Product(shopping, input, 2);
        // then
        assertEquals(7, product.id());
        assertEquals("product", product.name());
        assertEquals(true, product.quantitized());
        assertEquals(new BigDecimal("10.01"), product.quantity());
        assertEquals("kg", product.unit());
        LongList buys = product.buys();
        assertEquals(2, buys.size());
        long buy1 = buys.get(0);
        assertEquals(ONE_DAY_AGO, buy1);
        long buy2 = buys.get(1);
        assertEquals(TODAY, buy2);
        assertEquals(-1, product.distance());
    }
}
