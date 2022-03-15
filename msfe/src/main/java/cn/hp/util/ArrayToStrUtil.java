package cn.hp.util;

import java.util.List;

public class ArrayToStrUtil {
    public static String transfer(List<String> arr) {
        StringBuilder sb = new StringBuilder();
        for (String str: arr) {
            sb.append(str).append("|");
        }
        if (sb.toString().length() > 0) return sb.toString().substring(0, sb.length() - 1);
        return sb.toString();
    }
}
