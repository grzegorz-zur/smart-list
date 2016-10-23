/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.core;

import java.util.*;

class ImportanceComparator implements Comparator<Product> {

	private final Map<Product, Float> importances;

	ImportanceComparator(Map<Product, Float> importances) {
		this.importances = importances;
	}

	@Override
	public int compare(Product product1, Product product2) {
		float importance1 = 0;
		Float importanceObject1 = importances.get(product1);
		if (importanceObject1 != null) {
			importance1 = importanceObject1;
		}

		float importance2 = 0;
		Float importanceObject2 = importances.get(product2);
		if (importanceObject2 != null) {
			importance2 = importanceObject2;
		}

		return -Float.compare(importance1, importance2);
	}
}
