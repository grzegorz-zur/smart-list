/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongIterator;
import org.apache.commons.collections.primitives.LongList;
import org.apache.commons.collections.primitives.decorators.UnmodifiableLongList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class Product {

    final Shopping shopping;
    short id;
    String name;
    boolean quantitized;
    BigDecimal quantity;
    String unit;
    LongList buys;
    long distance;

    Product(Shopping shopping, short id, String name) {
        this.shopping = shopping;
        this.id = id;
        this.name = name;
        quantitized = false;
        quantity = BigDecimal.ONE;
        unit = "";
        buys = new ArrayLongList(1);
        distance = -1;
    }

    Product(Shopping shopping, DataInputStream stream, int version)
            throws IOException {
        this.shopping = shopping;
        if (version >= 3) {
            readV3(stream);
        } else if (version == 2) {
            readV2(stream);
        } else {
            readV1(stream);
        }
    }

    private void readV3(DataInputStream stream) throws IOException {
        id = stream.readShort();
        name = stream.readUTF();
        quantitized = stream.readBoolean();
        String quantityString = stream.readUTF();
        quantity = new BigDecimal(quantityString);
        unit = stream.readUTF();
        int size = stream.readInt();
        buys = new ArrayLongList(size + 1);
        for (int i = 0; i < size; ++i) {
            long time = stream.readLong();
            buys.add(time);
        }
        distance = stream.readLong();
    }

    private void readV2(DataInputStream stream) throws IOException {
        id = stream.readShort();
        name = stream.readUTF();
        quantitized = stream.readBoolean();
        String quantityString = stream.readUTF();
        quantity = new BigDecimal(quantityString);
        unit = stream.readUTF();
        int size = stream.readInt();
        buys = new ArrayLongList(size + 1);
        for (int i = 0; i < size; ++i) {
            long time = stream.readLong();
            buys.add(time);
        }
        distance = -1;
    }

    private void readV1(DataInputStream stream) throws IOException {
        id = stream.readShort();
        name = stream.readUTF();
        quantitized = false;
        quantity = BigDecimal.ONE;
        unit = "";
        int size = stream.readInt();
        buys = new ArrayLongList(size + 1);
        for (int i = 0; i < size; ++i) {
            long time = stream.readLong();
            buys.add(time);
        }
        distance = -1;
    }

    public void save(DataOutputStream stream) throws IOException {
        stream.writeShort(id);
        stream.writeUTF(name);
        stream.writeBoolean(quantitized);
        String quantityString = quantity.toString();
        stream.writeUTF(quantityString);
        stream.writeUTF(unit);
        stream.writeInt(buys.size());
        LongIterator buysIterator = buys.iterator();
        while (buysIterator.hasNext()) {
            long time = buysIterator.next();
            stream.writeLong(time);
        }
        stream.writeLong(distance);
    }

    public void quantitize(BigDecimal quantity, String unit) {
        quantitized = true;
        this.quantity = quantity;
        this.unit = unit;
    }

    public void dequantitize() {
        quantitized = false;
    }

    public void enlist() {
        shopping.enlist(this);
    }

    public void delist() {
        shopping.delist(this);
    }

    public boolean enlisted() {
        return shopping.onList(this);
    }

    public void buy(long time) {
        if (time < 0) {
            throw new IllegalArgumentException("time is less than zero");
        }

        int i = buys.size() - 1;
        while (i >= 0) {
            long buy = buys.get(i);
            if (buy < time) {
                break;
            }
            --i;
        }

        buys.add(i + 1, time);
        shopping.buy(this);
    }

    public void unBuy() {
        if (buys.isEmpty()) {
            return;
        }

        int position = buys.size() - 1;
        buys.removeElementAt(position);
    }

    public void rename(String name) {
        if (!Shopping.valid(name)) {
            throw new IllegalArgumentException("name is invalid");
        }
        if (shopping.exists(name)) {
            throw new IllegalArgumentException(
                    String.format("product %s exists", name));
        }

        String previousName = this.name;
        this.name = name;
        shopping.rename(this, previousName);
    }

    public void remove() {
        shopping.remove(this);
    }

    public long lastBuy() {
        if (buys.isEmpty()) {
            return Long.MIN_VALUE;
        }

        return buys.get(buys.size() - 1);
    }

    public long nextBuy() {
        if (buys.size() == 0) {
            return 0;
        }
        if (buys.size() == 1) {
            return Long.MAX_VALUE;
        }
        if (distance < 0) {
            return Long.MAX_VALUE;
        }

        long lastBuy = lastBuy();
        return lastBuy + distance;
    }

    public long timeLeft(long time) {
        if (buys.size() == 0) {
            return 0;
        }
        if (buys.size() == 1) {
            return Long.MAX_VALUE;
        }

        long nextBuy = nextBuy();
        return nextBuy - time;
    }

    public float supply(long time) {
        if (time < 0) {
            throw new IllegalArgumentException("time is less than zero");
        }

        if (buys.size() == 0) {
            return 0;
        }
        if (buys.size() == 1) {
            return Float.MAX_VALUE;
        }
        if (distance < 0) {
            return Float.MAX_VALUE;
        }

        long timeLeft = timeLeft(time);
        return (float) timeLeft / distance;
    }

    public short id() {
        return id;
    }

    public String name() {
        return name;
    }

    public boolean quantitized() {
        return quantitized;
    }

    public BigDecimal quantity() {
        return quantity;
    }

    public String unit() {
        return unit;
    }

    public LongList buys() {
        return UnmodifiableLongList.wrap(buys);
    }

    public long distance() {
        return distance;
    }

    @SuppressWarnings("NonFinalFieldReferenceInEquals")
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }

        Product other = (Product) object;
        return id == other.id;
    }

    @SuppressWarnings("NonFinalFieldReferencedInHashCode")
    @Override
    public int hashCode() {
        return id;
    }
}
