package cn.hp.util;

public class StrUtil {
    public static Integer computeCharNum(String str, Character ch) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) sum++;
        }
        return sum;
    }

    public static String pruneString(String originStr) {
        if (originStr.matches("\".*\"")) {
            return originStr.substring(1, originStr.length() - 1);
        }
        return originStr;
    }

    public static String capitalize(String originStr) {
        if (originStr.length() > 0) {
            return originStr.substring(0, 1).toUpperCase() + originStr.substring((1));
        }
        return originStr;
    }
}
