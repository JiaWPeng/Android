package com.jwpeng.mycamera.Uilts;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUilt {
    public static String getName(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "IMG_"+dateFormat.format(date);
    }
}
