package cn.hp.util;

public class StrUtil {
    public static Integer computeCharNum(String str, Character ch) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) sum++;
        }
        return sum;
    }
}
