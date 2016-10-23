/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SmallTest
public class StatisticsLoadSaveTest extends StatisticsTest {

    private InputOutput io;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        io = new InputOutput();
    }

    public void testLoadV2() throws IOException {
        // given
        DataOutputStream output = io.output();
        output.writeInt(2);
        output.writeInt(0x00000001);
        output.writeFloat(0.12F);
        output.writeInt(0x00000002);
        output.writeFloat(0.13F);
        DataInputStream input = io.input();
        // when
        Statistics statistics = new Statistics(input, 2);
        // then
        assertEquals(0.12F, statistics.correlation(product1, product2));
        assertEquals(0.13F, statistics.correlation(product1, product3));
        assertEquals(0F, statistics.correlation(product2, product3));
    }

    public void testLoadV3() throws IOException {
        // given
        DataOutputStream output = io.output();
        output.writeInt(2);
        output.writeInt(8);
        output.writeInt(10);
        output.writeInt(0);
        output.writeInt(0x00000001);
        output.writeInt(0x00000002);
        output.writeInt(0);
        output.writeInt(0);
        output.writeInt(0);
        output.writeInt(0);
        output.writeInt(0);
        output.writeInt(0);
        output.writeInt(0);
        output.writeFloat(0);
        output.writeFloat(0.12F);
        output.writeFloat(0.13F);
        output.writeFloat(0);
        output.writeFloat(0);
        output.writeFloat(0);
        output.writeFloat(0);
        output.writeFloat(0);
        output.writeFloat(0);
        output.writeFloat(0);
        output.writeShort(Short.MAX_VALUE);
        output.writeShort(Short.MAX_VALUE);
        output.writeShort(Short.MAX_VALUE);
        output.writeShort(Short.MAX_VALUE);
        output.writeShort(Short.MAX_VALUE);
        output.writeShort(Short.MAX_VALUE);
        output.writeShort(Short.MAX_VALUE);
        output.writeShort(Short.MAX_VALUE);
        output.writeShort(Short.MAX_VALUE);
        output.writeShort(Short.MAX_VALUE);
        DataInputStream input = io.input();
        // when
        Statistics statistics = new Statistics(input, 3);
        // then
        assertEquals(0.12F, statistics.correlation(product1, product2));
        assertEquals(0.13F, statistics.correlation(product1, product3));
        assertEquals(0F, statistics.correlation(product2, product3));
    }

    public void testSave() throws IOException {
        // given
        Statistics statistics = new Statistics();
        statistics.correlation(product1, product2, 0.12F);
        statistics.correlation(product1, product3, 0.13F);
        DataOutputStream output = io.output();
        // when
        statistics.save(output);
        // then
        DataInputStream input = io.input();
        assertEquals(2, input.readInt());
        assertEquals(6, input.readInt());
        assertEquals(8, input.readInt());
        assertEquals(0, input.readInt());
        assertEquals(0x00000001, input.readInt());
        assertEquals(0x00000002, input.readInt());
        assertEquals(0, input.readInt());
        assertEquals(0, input.readInt());
        assertEquals(0, input.readInt());
        assertEquals(0, input.readInt());
        assertEquals(0, input.readInt());
        assertEquals(0F, input.readFloat());
        assertEquals(0.12F, input.readFloat());
        assertEquals(0.13F, input.readFloat());
        assertEquals(0F, input.readFloat());
        assertEquals(0F, input.readFloat());
        assertEquals(0F, input.readFloat());
        assertEquals(0F, input.readFloat());
        assertEquals(0F, input.readFloat());
        assertEquals(Short.MIN_VALUE, input.readShort());
        assertEquals(Short.MIN_VALUE, input.readShort());
        assertEquals(Short.MIN_VALUE, input.readShort());
        assertEquals(Short.MIN_VALUE, input.readShort());
        assertEquals(Short.MIN_VALUE, input.readShort());
        assertEquals(Short.MIN_VALUE, input.readShort());
        assertEquals(Short.MIN_VALUE, input.readShort());
        assertEquals(Short.MIN_VALUE, input.readShort());
    }

    public void testSaveLoad() throws IOException {
        // given
        Statistics statistics = new Statistics();
        statistics.correlation(product1, product2, 0.12F);
        statistics.correlation(product1, product3, 0.13F);
        DataOutputStream output = io.output();
        // when
        statistics.save(output);
        DataInputStream input = io.input();
        statistics = new Statistics(input, 3);
        // then
        assertEquals(0.12F, statistics.correlation(product1, product2));
        assertEquals(0.13F, statistics.correlation(product1, product3));
        assertEquals(0F, statistics.correlation(product2, product3));
    }
}
