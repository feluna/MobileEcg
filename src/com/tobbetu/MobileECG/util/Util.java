package com.tobbetu.MobileECG.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kanilturgut on 27/03/14, 22:47.
 */
public class Util {

    public static Date stringToDate(String strDate) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String dateToString(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

}
