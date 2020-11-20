package com.ggboy.exam.utils;

import java.text.ParseException;
import java.util.Date;

public class DateUtil {

    public static Date getNowDate() throws ParseException {
        return new Date(System.currentTimeMillis());
    }

}
