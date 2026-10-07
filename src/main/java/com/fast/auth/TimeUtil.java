package com.fast.auth;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeUtil {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("EEE MMM dd yyyy");

    private TimeUtil() {}

    public static String now() {
        return FORMAT.format(new Date());
    }
}
