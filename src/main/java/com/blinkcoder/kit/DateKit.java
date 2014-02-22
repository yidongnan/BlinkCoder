package com.blinkcoder.kit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-2-15
 * Time: 上午11:45
 */
public class DateKit {
    private static final DateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd");

    public static String format(Date date) {
        return DATE_FORMAT_1.format(date);
    }

    /*public static String format(Timestamp timestamp) {
        return DATE_FORMAT_1.format(timestamp);
    }*/
}
