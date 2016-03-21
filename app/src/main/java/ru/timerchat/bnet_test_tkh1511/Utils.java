package ru.timerchat.bnet_test_tkh1511;

import java.text.SimpleDateFormat;

/**
 * Created by Timur on 22.03.16.
 */
public class Utils {

    public static String getFullDate(long date) {
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return fullDateFormat.format(date);
    }

}