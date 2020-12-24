package com.ggboy.exam.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UnixUtils {

    public static String toDateString(Long unix){
        Date date = new Date(unix*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static Long getUnix(){
        return System.currentTimeMillis()/1000L;
    }

}
