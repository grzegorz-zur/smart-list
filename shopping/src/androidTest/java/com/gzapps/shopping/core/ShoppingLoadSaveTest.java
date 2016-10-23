/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;
import static com.gzapps.shopping.core.Shopping.VERSION;
import static com.gzapps.shopping.core.Time.DAY;
import static com.gzapps.shopping.core.Time.WEEK;

@SmallTest
public class ShoppingLoadSaveTest extends ShoppingTest {

    private InputOutput io;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        io = new InputOutput();
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testCheckVersion() throws IOException {
        // given
        DataOutputStream output = io.output();
        output.writeInt(VERSION + 1);
        DataInputStream input = io.input();
        // when
        try {
            new Shopping(input);
            fail();
        } catch (IOException e) {
            // then
        }
    }

    public void testLoad() throws IOException {
        // given
        DataOutputStream output = io.output();
        output.writeInt(3);
        output.writeShort(3);
        output.writeBoolean(false);
        {
            output.writeInt(3);
            {
                output.writeShort(0);
                output.writeUTF("product0");
                output.writeBoolean(true);
                output.writeUTF("10");
                output.writeUTF("lb");
                output.writeInt(2);
                output.writeLong(ONE_DAY_AGO);
                output.writeLong(TODAY);
                output.writeLong(DAY);
            }
            {
                output.writeShort(1);
                output.writeUTF("product1");
                output.writeBoolean(false);
                output.writeUTF("10.01");
                output.writeUTF("kg");
                output.writeInt(1);
                output.writeLong(TODAY);
                output.writeLong(DAY);
            }
            {
                output.writeShort(2);
                output.writeUTF("product2");
                output.writeBoolean(false);
                output.writeUTF("1");
                output.writeUTF("");
                output.writeInt(0);
                output.writeLong(WEEK);
            }
        }
        {
            output.writeInt(2);
            output.writeShort(0);
            output.writeShort(2);
        }
        {
            output.writeInt(0);
            output.writeInt(1);
            output.writeInt(2);
            {
                output.writeInt(0);
                output.writeInt(0);
            }
            {
                output.writeFloat(0);
                output.writeFloat(0);
            }
            {
                output.writeShort(0);
                output.writeShort(0);
            }
        }
        DataInputStream input = io.input();
        // when
        Shopping shopping = new Shopping(input);
        // then
        assertEquals(3, shopping.nextId());
        assertEquals(3, shopping.products().size());
        assertEquals(2, shopping.list().size());
    }

    public void testSave() throws IOException {
        // given
        Shopping shopping = new Shopping();
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        Product product3 = shopping.create("product3");
        product1.buy(TWO_DAYS_AGO);
        product2.buy(ONE_DAY_AGO);
        product1.quantitize(new BigDecimal("0.003"), "kg");
        product1.enlist();
        product3.enlist();
        DataOutputStream output = io.output();
        // when
        shopping.save(output);
        // then
        DataInputStream input = io.input();
        assertEquals(3, input.readInt());
        assertEquals(2, input.readShort());
        assertEquals(true, input.readBoolean());
        {
            assertEquals(3, input.readInt());
            {
                assertEquals(0, input.readShort());
                assertEquals("product1", input.readUTF());
                assertEquals(true, input.readBoolean());
                assertEquals("0.003", input.readUTF());
                assertEquals("kg", input.readUTF());
                assertEquals(1, input.readInt());
                assertEquals(TWO_DAYS_AGO, input.readLong());
                assertEquals(-1, input.readLong());
            }
            {
                assertEquals(1, input.readShort());
                assertEquals("product2", input.readUTF());
                assertEquals(false, input.readBoolean());
                assertEquals("1", input.readUTF());
                assertEquals("", input.readUTF());
                assertEquals(1, input.readInt());
                assertEquals(ONE_DAY_AGO, input.readLong());
                assertEquals(-1, input.readLong());
            }
            {
                assertEquals(2, input.readShort());
                assertEquals("product3", input.readUTF());
                assertEquals(false, input.readBoolean());
                assertEquals("1", input.readUTF());
                assertEquals("", input.readUTF());
                assertEquals(0, input.readInt());
                assertEquals(-1, input.readLong());
            }
        }
        {
            assertEquals(2, input.readInt());
            assertEquals(0, input.readShort());
            assertEquals(2, input.readShort());
        }
        {
            assertEquals(0, input.readInt());
            assertEquals(6, input.readInt());
            assertEquals(8, input.readInt());
        }
    }

    public void testSaveLoad() throws IOException {
        // given
        Shopping shopping = new Shopping();
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        Product product3 = shopping.create("product3");
        product1.buy(TWO_DAYS_AGO);
        product2.buy(ONE_DAY_AGO);
        product1.enlist();
        product3.enlist();
        DataOutputStream output = io.output();
        // when
        shopping.save(output);
        DataInputStream input = io.input();
        shopping = new Shopping(input);
        // then
        assertEquals(3, shopping.nextId());
        assertEquals(3, shopping.products().size());
        assertEquals(2, shopping.list().size());
    }
}
