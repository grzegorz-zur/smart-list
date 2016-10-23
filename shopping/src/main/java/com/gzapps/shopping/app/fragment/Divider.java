/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.fragment;

import android.util.SparseArray;

class Divider {

    private static final int ALPHABET_SIZE = 42;
    private static final SparseArray<Divider> DIVIDERS =
            new SparseArray<Divider>(ALPHABET_SIZE);
    final int id;
    final String name;

    private Divider(char character) {
        id = Short.MAX_VALUE + 1 + character;
        name = String.valueOf(character);
    }

    static Divider create(char character) {
        Divider divider = DIVIDERS.get(character);
        if (divider == null) {
            divider = new Divider(character);
            DIVIDERS.append(character, divider);
        }
        return divider;
    }
}
