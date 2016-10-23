/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.core;

import java.text.*;
import java.util.*;

class NameComparator implements Comparator<Product> {

	private final Collator collator;

	NameComparator() {
		collator = Collator.getInstance();
	}

	@Override
	public int compare(Product product1, Product product2) {
		String name1 = product1.name();
		String name2 = product2.name();
		return collator.compare(name1, name2);
	}

}
