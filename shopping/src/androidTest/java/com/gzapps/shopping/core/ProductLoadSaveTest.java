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
import static com.gzapps.shopping.core.Shopping.VERSION;
import static com.gzapps.shopping.core.Time.DAY;

@SmallTest
public class ProductLoadSaveTest extends ProductTest {

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
        output.writeLong(DAY);
        DataInputStream input = io.input();
        // when
        Product product = new Product(shopping, input, VERSION);
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
        assertEquals(DAY, product.distance());
    }

    public void testSave() throws IOException {
        // given
        Product product = new Product(shopping, (short) 13, "product");
        product.buy(ONE_DAY_AGO);
        product.buy(TODAY);
        product.quantitize(new BigDecimal("1.1"), "unit");
        product.enlist();
        product.distance = DAY;
        DataOutputStream output = io.output();
        // when
        product.save(output);
        // then
        DataInputStream input = io.input();
        assertEquals(13, input.readShort());
        assertEquals("product", input.readUTF());
        assertEquals(true, input.readBoolean());
        assertEquals("1.1", input.readUTF());
        assertEquals("unit", input.readUTF());
        assertEquals(2, input.readInt());
        assertEquals(ONE_DAY_AGO, input.readLong());
        assertEquals(TODAY, input.readLong());
        assertEquals(DAY, input.readLong());
    }

    public void testSaveLoad() throws IOException {
        // given
        Product product = new Product(shopping, (short) 13, "product");
        product.buy(ONE_DAY_AGO);
        product.buy(TODAY);
        product.quantitize(new BigDecimal("3.14"), "l");
        product.distance = DAY;
        product.enlist();
        DataOutputStream output = io.output();
        // when
        product.save(output);
        DataInputStream input = io.input();
        product = new Product(shopping, input, VERSION);
        // then
        assertEquals(13, product.id());
        assertEquals("product", product.name());
        assertEquals(true, product.quantitized());
        assertEquals(new BigDecimal("3.14"), product.quantity());
        assertEquals("l", product.unit());
        assertEquals(2, product.buys().size());
        LongList buys = product.buys();
        long buy1 = buys.get(0);
        assertEquals(ONE_DAY_AGO, buy1);
        long buy2 = buys.get(1);
        assertEquals(TODAY, buy2);
        assertEquals(DAY, product.distance());
    }
}
