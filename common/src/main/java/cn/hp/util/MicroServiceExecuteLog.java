package cn.hp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MicroServiceExecuteLog {
    private static StringBuilder log = new StringBuilder();

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void info(String msg) {
        log.append("[INFO]").append(" ")
                .append(getCurrentTime()).append(" ")
                .append(msg).append("\n");
    }

    public static void error(String msg) {
        log.append("[ERROR]").append(" ")
                .append(getCurrentTime()).append(" ")
                .append(msg).append("\n");
    }

    public static String getLog() {
        return log.toString();
    }

    public static void init() {
        log = new StringBuilder();
    }

    private static String getCurrentTime() {
        return simpleDateFormat.format(new Date());
    }
}
