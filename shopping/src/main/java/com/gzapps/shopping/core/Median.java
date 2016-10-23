/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.core;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongList;

class Median {

    private final LongList list;

    Median(int capacity) {
        list = new ArrayLongList(capacity);
    }

    void add(long value) {
        list.add(value);
    }

    void add(long[] values) {
        for (long value : values) {
            add(value);
        }
    }

    void clear() {
        list.clear();
    }

    boolean isEmpty() {
        return list.isEmpty();
    }

    long calculate() {
        if (list.size() == 0) {
            throw new IllegalStateException("list is empty");
        }

        int position = (list.size() - 1) / 2;
        int left = 0;
        int right = list.size() - 1;

        while (left < right) {
            int index = left + (right - left) / 2;
            long value = list.get(index);

            swap(index, right);
            index = left;
            for (int i = left; i < right; ++i) {
                if (list.get(i) < value) {
                    swap(i, index);
                    ++index;
                }
            }
            swap(index, right);

            if (index < position) {
                left = index + 1;
            } else if (index > position) {
                right = index - 1;
            } else {
                return value;
            }
        }
        return list.get(left);
    }

    private void swap(int i, int j) {
        long x = list.get(i);
        list.set(i, list.get(j));
        list.set(j, x);
    }
}
