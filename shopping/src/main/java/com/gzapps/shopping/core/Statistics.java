/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.core;

import java.io.*;

import static com.gzapps.shopping.core.Time.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;

public class Statistics {

	public static final float CORRELATION_DEFAULT = 0F;

	public static final int DISTANCE_DEFAULT = (int) (DAY / SECOND);

	private static final float RATIO = 0.8F;

	private static final int LENGTH = 8;

	private int size;

	private int limit;

	private int[] keys;

	private float[] correlations;

	private short[] distances;

	Statistics() {
		init(0);
	}

	Statistics(int capacity) {
		init(capacity);
	}

	Statistics(DataInputStream stream, int version) throws IOException {
		if (version >= 3) {
			readV3(stream);
		} else {
			readV2(stream);
		}
	}

	private void init(int capacity) {
		int length = (int) ceil(capacity / RATIO) + LENGTH;

		size = 0;
		keys = new int[length];
		correlations = new float[length];
		distances = new short[length];
		limit = (int) (RATIO * length);

		fill(distances, Short.MIN_VALUE);
	}

	private void readV2(DataInputStream stream) throws IOException {
		int count = stream.readInt();
		init(count);
		for (int i = 0; i < count; ++i) {
			int key = stream.readInt();
			float correlation = stream.readFloat();
			int index = allocate(key);
			correlations[index] = correlation;
		}
	}

	private void readV3(DataInputStream stream) throws IOException {
		size = stream.readInt();
		limit = stream.readInt();
		int length = stream.readInt();

		keys = new int[length];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = stream.readInt();
		}

		correlations = new float[length];
		for (int i = 0; i < correlations.length; i++) {
			correlations[i] = stream.readFloat();
		}

		distances = new short[length];
		fill(distances, Short.MIN_VALUE);
		for (int i = 0; i < distances.length; i++) {
			distances[i] = stream.readShort();
		}
	}

	public void save(DataOutputStream stream) throws IOException {
		stream.writeInt(size);
		stream.writeInt(limit);
		stream.writeInt(keys.length);

		for (int key : keys) {
			stream.writeInt(key);
		}
		for (float correlation : correlations) {
			stream.writeFloat(correlation);
		}
		for (short distance : distances) {
			stream.writeShort(distance);
		}
	}

	public float correlation(Product product1, Product product2) {
		if (product1.equals(product2)) {
			return 1;
		}

		int key = key(product1.id(), product2.id());
		int index = find(key);
		if (index < 0) {
			return CORRELATION_DEFAULT;
		}

		return correlations[index];
	}

	public int distance(Product product1, Product product2) {
		if (product1.equals(product2)) {
			return 0;
		}

		int key = key(product1.id(), product2.id());
		int index = find(key);
		if (index < 0) {
			return DISTANCE_DEFAULT;
		}

		return distances[index] + Short.MAX_VALUE;
	}

	public void correlation(Product product1, Product product2,
			float correlation) {
		if (product1.equals(product2)) {
			return;
		}

		int key = key(product1.id(), product2.id());
		int index = find(key);
		if (index < 0) {
			index = allocate(key);
		}

		correlations[index] = correlation;
	}

	public void distance(Product product1, Product product2, int distance) {
		if (product1.equals(product2)) {
			return;
		}

		int key = key(product1.id(), product2.id());
		int index = find(key);
		if (index < 0) {
			index = allocate(key);
		}

		int value = distance - Short.MAX_VALUE;
		distances[index] = (short) value;
	}

	private int find(int key) {
		int hash = hash(key);
		int index = abs(hash) % keys.length;
		while (keys[index] != key) {
			if (keys[index] == 0) {
				return -1;
			}
			index = (index + 1) % keys.length;
		}
		return index;
	}

	private int allocate(int key) {
		if (size + 1 > limit) {
			resize();
		}

		int hash = hash(key);
		int index = abs(hash) % keys.length;
		while (keys[index] != 0) {
			index = (index + 1) % keys.length;
		}

		keys[index] = key;
		size += 1;

		return index;
	}

	private void resize() {
		int[] oldKeys = keys;
		float[] oldCorrelations = correlations;
		short[] oldDistances = distances;

		keys = new int[keys.length * 2];
		correlations = new float[correlations.length * 2];
		distances = new short[distances.length * 2];
		limit = (int) (RATIO * keys.length);

		fill(distances, Short.MIN_VALUE);

		for (int i = 0; i < oldKeys.length; ++i) {
			int key = oldKeys[i];
			if (key != 0) {
				int hash = hash(key);
				int index = abs(hash) % keys.length;
				while (keys[index] != 0) {
					index = (index + 1) % keys.length;
				}

				keys[index] = key;
				correlations[index] = oldCorrelations[i];
				distances[index] = oldDistances[i];
			}
		}
	}

	public int size() {
		return size;
	}

	static int hash(int value) {
		return value;
	}

	@SuppressWarnings("MagicNumber")
	static int key(short id1, short id2) {
		if (id1 <= id2) {
			return id1 << 16 | id2;
		} else {
			return id2 << 16 | id1;
		}
	}
}
