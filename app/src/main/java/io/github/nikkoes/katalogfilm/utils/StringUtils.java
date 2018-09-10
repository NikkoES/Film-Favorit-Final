package io.github.nikkoes.katalogfilm.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {

    public static String dateConverter(String dateString) {
        DateFormat currentDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat newDateString = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date = currentDateString.parse(dateString);
            return newDateString.format(date);
        }
        catch (ParseException e) {
            Log.e("ASDF", e.toString());
        }

        return "";
    }

}
