/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Shopping {

    static final int VERSION = 3;
    final SortedSet<Product> products;
    final List<Product> list;
    final Map<Short, Product> productsById;
    final Map<String, Product> productsByName;
    final Set<Short> listById;
    final List<Product> buys;
    short nextId;
    boolean analyze;
    Statistics statistics;

    public Shopping() {
        NameComparator nameComparator = new NameComparator();
        products = new TreeSet<Product>(nameComparator);
        list = new ArrayList<Product>();
        statistics = new Statistics();
        productsById = new HashMap<Short, Product>();
        productsByName = new HashMap<String, Product>();
        listById = new HashSet<Short>();
        buys = new LinkedList<Product>();
    }

    public Shopping(DataInputStream stream) throws IOException {
        int version = stream.readInt();
        if (version > VERSION) {
            throw new IOException("wrong file version");
        }

        nextId = stream.readShort();
        if (version >= 2) {
            analyze = stream.readBoolean();
        }

        int productsSize = stream.readInt();
        NameComparator nameComparator = new NameComparator();
        products = new TreeSet<Product>(nameComparator);
        productsById = new HashMap<Short, Product>(productsSize);
        productsByName = new HashMap<String, Product>(productsSize);
        for (int i = 0; i < productsSize; ++i) {
            Product product = new Product(this, stream, version);
            products.add(product);
            productsById.put(product.id(), product);
            productsByName.put(product.name(), product);
        }

        int listSize = stream.readInt();
        list = new ArrayList<Product>(listSize);
        listById = new HashSet<Short>(listSize);
        for (int i = 0; i < listSize; ++i) {
            short id = stream.readShort();
            Product product = productsById.get(id);
            list.add(product);
            listById.add(product.id());
        }

        buys = new LinkedList<Product>();

        statistics = new Statistics(stream, version);

        if (version < 3) {
            long time = System.currentTimeMillis();
            Analysis analysis = new Analysis(this, time);
            for (Product product : products) {
                analysis.calculateDistance(product);
            }
            analyze = true;
        }
    }

    public static boolean valid(String name) {
        return name != null && name.trim().length() > 0;
    }

    public void save(DataOutputStream stream) throws IOException {
        stream.writeInt(VERSION);

        stream.writeShort(nextId);
        stream.writeBoolean(analyze);

        stream.writeInt(products.size());
        for (Product product : products) {
            product.save(stream);
        }

        stream.writeInt(list.size());
        for (Product product : list) {
            stream.writeShort(product.id());
        }

        statistics.save(stream);
    }

    public Product create(String name) {
        if (!valid(name)) {
            throw new IllegalArgumentException("invalid product name");
        }
        if (exists(name)) {
            throw new IllegalArgumentException(
                    String.format("product %s already exists", name));
        }

        short id = nextId();
        Product product = new Product(this, id, name);
        products.add(product);
        productsById.put(product.id(), product);
        productsByName.put(product.name(), product);

        return product;
    }

    short nextId() {
        while (productsById.containsKey(nextId)) {
            ++nextId;
        }
        return nextId;
    }

    public void enlist(Product product) {
        if (list.contains(product)) {
            return;
        }

        int position = position(product);
        list.add(position, product);
        listById.add(product.id());
    }

    public void delist(Product product) {
        list.remove(product);
        listById.remove(product.id());
    }

    public boolean onList(Product product) {
        return listById.contains(product.id());
    }

    int position(Product product) {
        if (list.size() == 0) {
            return 0;
        }

        if (list.size() == 1) {
            return 1;
        }

        int closestPosition = list.size();
        int closestDistance = Integer.MAX_VALUE;

        for (int position = list.size() - 1; position >= 0; --position) {
            Product currentProduct = list.get(position);
            int distance = statistics.distance(product, currentProduct);
            if (distance < closestDistance) {
                closestPosition = position;
                closestDistance = distance;
            }
        }

        if (closestPosition == 0) {
            Product closestProduct = list.get(closestPosition);
            Product nextProduct = list.get(closestPosition + 1);
            int closestToNext =
                    statistics.distance(closestProduct, nextProduct);
            int productToNext = statistics.distance(product, nextProduct);
            if (closestDistance + productToNext < closestToNext) {
                return closestPosition + 1;
            } else {
                return closestPosition;
            }
        }

        if (closestPosition == list.size() - 1) {
            Product closestProduct = list.get(closestPosition);
            Product previousProduct = list.get(closestPosition - 1);
            int closestToPrevious =
                    statistics.distance(closestProduct, previousProduct);
            int productToNext = statistics.distance(product, previousProduct);
            if (closestDistance + productToNext < closestToPrevious) {
                return closestPosition;
            } else {
                return closestPosition + 1;
            }
        }

        Product previousProduct = list.get(closestPosition - 1);
        Product nextProduct = list.get(closestPosition + 1);

        int previousDistance = statistics.distance(product, previousProduct);
        int nextDistance = statistics.distance(product, nextProduct);

        if (previousDistance < nextDistance) {
            return closestPosition;
        } else {
            return closestPosition + 1;
        }
    }

    public void buyAll(long time) {
        List<Product> listCopy = new ArrayList<Product>(list);
        for (Product product : listCopy) {
            product.buy(time);
        }
    }

    public void buy(Product product) {
        analyze = true;
        list.remove(product);
        listById.remove(product.id());
        buys.add(product);
    }

    public void unBuy() {
        if (buys.isEmpty()) {
            return;
        }

        int position = buys.size() - 1;
        Product product = buys.remove(position);
        product.unBuy();

        product.enlist();
    }

    public boolean canUnBuy() {
        return !buys.isEmpty();
    }

    public void rename(Product product, String previousName) {
        productsByName.remove(previousName);
        productsByName.put(product.name(), product);
    }

    public void remove(Product product) {
        list.remove(product);
        listById.remove(product.id());
        products.remove(product);
        productsById.remove(product.id());
        productsByName.remove(product.name());
    }

    public void clearList() {
        List<Product> listCopy = new ArrayList<Product>(list);
        for (Product product : listCopy) {
            product.delist();
        }
    }

    public void clearProducts() {
        List<Product> productsCopy = new ArrayList<Product>(products);
        for (Product product : productsCopy) {
            product.remove();
        }
    }

    public Product find(short id) {
        return productsById.get(id);
    }

    public Product find(String name) {
        return productsByName.get(name);
    }

    public List<Product> list() {
        return new ArrayList<Product>(list);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isListEmpty() {
        return list.isEmpty();
    }

    public List<Product> suggestions(long time, float correlationLimit,
                                     float supplyLimit) {
        List<Product> suggestions = new ArrayList<Product>(products.size());
        Map<Product, Float> importances =
                new HashMap<Product, Float>(products.size());

        for (Product product : products) {
            if (product.enlisted()) {
                continue;
            }

            float supply = product.supply(time);
            if (supply > supplyLimit) {
                continue;
            }

            float importance = 0;
            boolean correlationLimitReached = false;
            for (Product enlisted : list) {
                float correlation = statistics.correlation(product, enlisted);
                correlationLimitReached |= correlation >= correlationLimit;
                importance += correlation;
            }

            if (!correlationLimitReached) {
                continue;
            }

            suggestions.add(product);
            importances.put(product, importance);
        }

        Comparator<Product> comparator = new ImportanceComparator(importances);
        Collections.sort(suggestions, comparator);

        return suggestions;
    }

    public List<Product> shortages(long time, float lowerBound, float upperBound) {
        List<Product> shortages = new ArrayList<Product>(products.size());
        Map<Product, Float> importances =
                new HashMap<Product, Float>(products.size());

        for (Product product : products) {
            if (product.enlisted()) {
                continue;
            }

            float supply = product.supply(time);
            if (supply < lowerBound || upperBound < supply) {
                continue;
            }

            shortages.add(product);
            float importance = -supply;
            importances.put(product, importance);
        }

        Comparator<Product> comparator = new ImportanceComparator(importances);
        Collections.sort(shortages, comparator);

        return shortages;
    }

    public List<Product> products() {
        return new ArrayList<Product>(products);
    }

    public int productsCount() {
        return products.size();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isEmpty() {
        return products.isEmpty();
    }

    public boolean exists(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }

        return productsByName.containsKey(name);
    }

    public boolean analyze() {
        return analyze;
    }

    public void swap(Product product1, Product product2) {
        if (product1 == null || product2 == null)
            throw new IllegalArgumentException("product is null");

        if (product1.equals(product2))
            return;

        if (!onList(product1) || !onList(product2))
            return;

        int index1 = list.indexOf(product1);
        int index2 = list.indexOf(product2);

        list.set(index1, product2);
        list.set(index2, product1);
    }
}
