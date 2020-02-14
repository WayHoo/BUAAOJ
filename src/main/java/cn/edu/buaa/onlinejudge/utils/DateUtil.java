package cn.edu.buaa.onlinejudge.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static String FORMATTER_SECOND = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前Timestamp
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前时间，使用默认时间格式
     * @return
     */
    public static String getCurrentTime(){
        return getCurrentTime(FORMATTER_SECOND);
    }

    /**
     * 获取当前时间
     * @param format - 时间格式字符串
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 将Timestamp转换成yyyy-MM-dd HH:mm:ss格式的字符串
     * 并去掉默认秒数值中的小数部分
     * @param timestamp
     * @return
     */
    public static String formatTimestamp(Timestamp timestamp){
        return timestamp.toString().substring(0,timestamp.toString().indexOf("."));
    }
}
