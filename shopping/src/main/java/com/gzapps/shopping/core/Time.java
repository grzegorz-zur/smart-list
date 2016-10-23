/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.core;

public interface Time {

	long SECOND = 1000;

	long MINUTE = 60 * SECOND;

	long HOUR = 60 * MINUTE;

	long DAY = 24 * HOUR;

	long WEEK = 7 * DAY;

	long MONTH = 30 * DAY;

	long YEAR = 365 * DAY;
}
