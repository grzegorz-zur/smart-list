/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.util.Log;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongIterator;
import org.apache.commons.collections.primitives.LongList;
import org.apache.commons.collections.primitives.LongListIterator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.util.FloatMath.sqrt;
import static com.gzapps.shopping.core.Time.DAY;
import static com.gzapps.shopping.core.Time.SECOND;
import static com.gzapps.shopping.core.Time.YEAR;
import static java.lang.Math.abs;

public class Analysis {

    public static final long LIMIT = YEAR;
    private static final int DATES = (int) (LIMIT / DAY);
    final Shopping shopping;
    final Statistics statistics;
    final long time;
    final boolean purge;
    final StopCallback callback;
    private final Map<Product, LongList> dates =
            new HashMap<Product, LongList>();
    private final Median median = new Median(DATES);
    private final Calendar calendar = Calendar.getInstance();

    public Analysis(Shopping shopping, long time, boolean purge, StopCallback callback) {
        this.shopping = shopping;
        this.time = time;
        this.purge = purge;
        this.callback = callback;
        int capacity = shopping.statistics.size() / 2;
        statistics = new Statistics(capacity);
    }

    public Analysis(Shopping shopping, long time, boolean purge) {
        this(shopping, time, purge, new StopCallback());
    }

    public Analysis(Shopping shopping, long time) {
        this(shopping, time, false, new StopCallback());
    }

    static int countCorrelated(LongList dates1, LongList dates2) {
        if (dates1.isEmpty() || dates2.isEmpty()) {
            return 0;
        }

        LongIterator iterator1 = dates1.iterator();
        LongIterator iterator2 = dates2.iterator();

        long date1 = iterator1.next();
        long date2 = iterator2.next();

        int count = 0;
        while (true) {
            long difference = date1 - date2;

            if (difference == 0) {
                ++count;
            }

            if (difference <= 0) {
                if (!iterator1.hasNext()) {
                    break;
                }
                date1 = iterator1.next();
            }

            if (difference >= 0) {
                if (!iterator2.hasNext()) {
                    break;
                }
                date2 = iterator2.next();
            }
        }
        return count;
    }

    static void mergeDates(LongList source, LongList target) {
        int index = 0;
        LongListIterator sourceIterator = source.listIterator();
        while (sourceIterator.hasNext()) {
            long date = sourceIterator.next();
            while (index < target.size() && target.get(index) < date) {
                ++index;
            }
            if (index >= target.size() || target.get(index) != date) {
                target.add(index, date);
            }
        }
    }

    static float correlation(float count1, float count2, float correlated,
                             float count) {
        if (count == 0) {
            return 0;
        }
        float ex = count1 / count;
        float ey = count2 / count;
        float exy = correlated / count;
        float dx = ex - ex * ex; // ex2 = ex
        float dy = ey - ey * ey; // ey2 = ey
        float divisor = sqrt(dx * dy);
        if (abs(divisor) == 0.0) {
            return 0;
        }
        float covariance = exy - ex * ey;
        return covariance / divisor; // correlation
    }

    public void analyze() {
        try {
            analyzeProducts();
            analyzeCorrelationsAndDistances();
            shopping.statistics = statistics;
            shopping.analyze = false;
        } catch (StopException stop) {
            Log.i("SmartList", "analysis stopped");
        }
    }

    void analyzeProducts() throws StopException {
        for (Product product : shopping.products) {
            callback.stop();
            truncateBuys(product);
        }
        if (purge) {
            Set<Product> unused = new HashSet<Product>(shopping.products.size());
            for (Product product : shopping.products) {
                callback.stop();
                if (!product.enlisted() && product.buys.isEmpty()) {
                    unused.add(product);
                }
            }
            for (Product product : unused) {
                callback.stop();
                product.remove();
            }
        }
        for (Product product : shopping.products) {
            callback.stop();
            calculateDistance(product);
            calculateDates(product);
        }
    }

    void truncateBuys(Product product) {
        long limit = time - LIMIT;
        LongIterator buysIterator = product.buys.iterator();
        while (buysIterator.hasNext()) {
            long buyTime = buysIterator.next();
            if (buyTime >= limit) {
                return;
            }
            buysIterator.remove();
        }
    }

    void calculateDistance(Product product) {
        LongList buys = product.buys;
        if (buys.size() < 2) {
            product.distance = -1;
            return;
        }

        median.clear();

        long last = buys.get(0);
        for (int i = 1; i < buys.size(); ++i) {
            long current = buys.get(i);
            long distance = abs(last - current);
            last = current;
            median.add(distance);
        }

        product.distance = median.calculate();
    }

    void calculateDates(Product product) {
        LongList dates = dates(product.buys);
        this.dates.put(product, dates);
    }

    void analyzeCorrelationsAndDistances() throws StopException {
        int buysDatesCount = datesCount();
        for (Product product1 : shopping.products) {
            for (Product product2 : shopping.products) {
                if (product1.id() < product2.id()) {
                    callback.stop();
                    analyzeCorrelation(product1, product2, buysDatesCount);
                    callback.stop();
                    analyzeDistance(product1, product2);
                }
            }
        }
    }

    void analyzeCorrelation(Product product1, Product product2, int count) {
        if (count == 0 || product1.buys.isEmpty() || product2.buys.isEmpty()) {
            return;
        }

        LongList dates1 = dates.get(product1);
        LongList dates2 = dates.get(product2);

        int correlatedCount = countCorrelated(dates1, dates2);

        float correlation =
                correlation(dates1.size(), dates2.size(), correlatedCount,
                        count);

        if (correlation > 0) {
            statistics.correlation(product1, product2, correlation);
        }
    }

    void analyzeDistance(Product product1, Product product2) {
        if (product1.buys.isEmpty() || product2.buys.isEmpty()) {
            return;
        }

        float distance = distance(product1.buys, product2.buys);

        if (distance >= 0 && distance < DAY) {
            float distanceInSeconds = distance / SECOND;
            statistics.distance(product1, product2, (int) distanceInSeconds);
        }
    }

    long distance(LongList times1, LongList times2) {
        if (times1.isEmpty() || times2.isEmpty()) {
            return -1;
        }

        median.clear();

        LongIterator iterator1 = times1.iterator();
        LongIterator iterator2 = times2.iterator();

        long time1 = iterator1.next();
        long time2 = iterator2.next();

        while (true) {
            if (sameDate(time1, time2)) {
                long distance = abs(time1 - time2);
                median.add(distance);
            }

            long difference = time1 - time2;

            if (difference <= 0) {
                if (!iterator1.hasNext()) {
                    break;
                }
                time1 = iterator1.next();
            }

            if (difference >= 0) {
                if (!iterator2.hasNext()) {
                    break;
                }
                time2 = iterator2.next();
            }
        }

        if (median.isEmpty()) {
            return -1;
        }

        return median.calculate();
    }

    int datesCount() {
        LongList dates = new ArrayLongList(DATES);
        for (Product product : shopping.products) {
            mergeDates(this.dates.get(product), dates);
        }
        return dates.size();
    }

    LongList dates(LongList times) {
        LongList dates = new ArrayLongList(DATES);
        LongIterator buysIterator = times.iterator();
        while (buysIterator.hasNext()) {
            long time = buysIterator.next();
            long date = date(time);
            if (dates.isEmpty() || dates.get(dates.size() - 1) != date) {
                dates.add(date);
            }
        }
        return dates;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    boolean sameDate(long time1, long time2) {
        if (time1 == time2) {
            return true;
        }

        long difference = abs(time2 - time1);
        if (difference >= DAY) {
            return false;
        }

        return date(time1) == date(time2);
    }

    long date(long time) {
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
