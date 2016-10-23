/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.fragment;

import com.gzapps.shopping.core.Product;

import java.util.ArrayList;
import java.util.List;

class Item {

    Product product;
    Divider divider;
    boolean checked;

    Item(Product product) {
        this.product = product;
    }

    Item(Divider divider) {
        this.divider = divider;
    }

    static List<Item> itemize(List<Product> products) {
        List<Item> items = new ArrayList<Item>(products.size());
        for (Product product : products) {
            Item item = new Item(product);
            items.add(item);
        }
        return items;
    }

    static List<Item> separate(List<Product> products) {
        List<Item> items = new ArrayList<Item>(products.size());

        char previousCharacter = 0;

        for (Product product : products) {
            String name = product.name();
            char character = Character.toUpperCase(name.charAt(0));

            if (previousCharacter != character) {
                Divider divider = Divider.create(character);
                Item item = new Item(divider);
                items.add(item);
            }
            Item item = new Item(product);
            items.add(item);

            String previousName = product.name();
            previousCharacter = Character.toUpperCase(previousName.charAt(0));
        }

        return items;
    }
}

