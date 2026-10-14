package com.example.activerevision.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    public static long calculateScheduledAt(long baseTimeMillis, int daysOffset) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(baseTimeMillis);
        cal.add(Calendar.DAY_OF_MONTH, daysOffset);
        return cal.getTimeInMillis();
    }

    public static String formatDateTime(long epochMilli) {
        if (epochMilli <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        return sdf.format(new Date(epochMilli));
    }

    public static String formatDate(long epochMilli) {
        if (epochMilli <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(epochMilli));
    }

    public static String formatTime(long epochMilli) {
        if (epochMilli <= 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(epochMilli));
    }

    public static long getStartOfDay(long epochMilli) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(epochMilli);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getEndOfDay(long epochMilli) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(epochMilli);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
}
