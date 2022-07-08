package ie.ul.foodapp.utils;

import java.time.LocalTime;

public class StringUtils {

    public static String toString(LocalTime lt) {
        return String.format("%02d:%02d", lt.getHour(), lt.getMinute());
    }

    public static String priceToString(double d) {
        return String.format("€%.02f", d);
    }

    public static String pickupTime(LocalTime lt) {
        return "Up to grab at " + lt.toString();
    }

}
