/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;

@SmallTest
public class ShoppingLoadVersion1Test extends ShoppingTest {

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
        output.writeInt(1);
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
        output.writeInt(1);
        output.writeShort(3);
        {
            output.writeInt(3);
            {
                output.writeShort(0);
                output.writeUTF("product0");
                output.writeInt(2);
                output.writeLong(ONE_DAY_AGO);
                output.writeLong(TODAY);
            }
            {
                output.writeShort(1);
                output.writeUTF("product1");
                output.writeInt(1);
                output.writeLong(TODAY);
            }
            {
                output.writeShort(2);
                output.writeUTF("product2");
                output.writeInt(0);
            }
        }
        {
            output.writeInt(2);
            output.writeShort(0);
            output.writeShort(2);
        }
        {
            output.writeInt(1);
            output.writeInt(1);
            output.writeFloat(0.01F);
        }
        DataInputStream input = io.input();
        // when
        Shopping shopping = new Shopping(input);
        // then
        assertEquals(3, shopping.nextId());
        assertEquals(3, shopping.products().size());
        assertEquals(2, shopping.list().size());
    }
}
