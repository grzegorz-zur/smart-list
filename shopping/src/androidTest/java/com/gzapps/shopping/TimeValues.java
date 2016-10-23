/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping;

import java.util.Calendar;

import static com.gzapps.shopping.core.Time.DAY;
import static com.gzapps.shopping.core.Time.WEEK;
import static com.gzapps.shopping.core.Time.YEAR;

@SuppressWarnings({"UtilityClass", "UtilityClassWithoutPrivateConstructor"})
public class TimeValues {

    public static final long TODAY;
    public static final long ONE_DAY_AGO;
    public static final long TWO_DAYS_AGO;
    public static final long THREE_DAYS_AGO;
    public static final long FOUR_DAYS_AGO;
    public static final long FIVE_DAYS_AGO;
    public static final long SIX_DAYS_AGO;
    public static final long SEVEN_DAYS_AGO;
    public static final long ONE_WEEK_AGO;
    public static final long TWO_WEEKS_AGO;
    public static final long THREE_WEEKS_AGO;
    public static final long FOUR_WEEKS_AGO;
    public static final long FIVE_WEEKS_AGO;
    public static final long ONE_YEAR_AGO;
    public static final long ONE_DAY_AHEAD;
    public static final long TWO_DAYS_AHEAD;
    public static final long THREE_DAYS_AHEAD;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.SECOND, 14);
        calendar.set(Calendar.MILLISECOND, 15);

        TODAY = calendar.getTimeInMillis();

        ONE_DAY_AGO = TODAY - 1 * DAY;
        TWO_DAYS_AGO = TODAY - 2 * DAY;
        THREE_DAYS_AGO = TODAY - 3 * DAY;
        FOUR_DAYS_AGO = TODAY - 4 * DAY;
        FIVE_DAYS_AGO = TODAY - 5 * DAY;
        SIX_DAYS_AGO = TODAY - 6 * DAY;
        SEVEN_DAYS_AGO = TODAY - 7 * DAY;
        ONE_WEEK_AGO = TODAY - 1 * WEEK;
        TWO_WEEKS_AGO = TODAY - 2 * WEEK;
        THREE_WEEKS_AGO = TODAY - 3 * WEEK;
        FOUR_WEEKS_AGO = TODAY - 4 * WEEK;
        FIVE_WEEKS_AGO = TODAY - 5 * WEEK;
        ONE_YEAR_AGO = TODAY - 1 * YEAR;
        ONE_DAY_AHEAD = TODAY + 1 * DAY;
        TWO_DAYS_AHEAD = TODAY + 2 * DAY;
        THREE_DAYS_AHEAD = TODAY + 3 * DAY;
    }
}
