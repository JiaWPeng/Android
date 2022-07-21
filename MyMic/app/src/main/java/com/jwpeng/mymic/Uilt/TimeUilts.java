package com.jwpeng.mymic.Uilt;

import java.text.SimpleDateFormat;

public class TimeUilts {
    public static String long2String(long time){
        // 毫转秒
        int sec = (int) (time/1000);
        // 分钟
        int min =sec/60;
        // 秒
        sec =sec%60;
        if (min <10){
            if (sec <10){
                return "0"+min+":0"+sec;
            }else {
                return "0"+min+":"+sec;
            }
            }else {
            if (sec <10){
                return min+":0"+sec;
            }else {
                return min+":"+sec;
            }
        }
    }

    /**
     * 返回当前格式为 yyyy/MM/dd HH:mm:ss
     * @return
     */
    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(System.currentTimeMillis());
    }

}
