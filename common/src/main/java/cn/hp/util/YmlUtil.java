package cn.hp.util;

import java.util.Map;

public class YmlUtil {
    public static String getProperty(Map yml, String property) {
        String[] properties = property.split("\\.");
        for (int i = 0; i < properties.length; i++) {
            if (null == yml) return null;
            if (i == properties.length - 1) {
                if (null == yml.get(properties[i])) return null;
                return yml.get(properties[i]).toString();
            }
            yml = (Map)yml.get(properties[i]);
        }
        return null;
    }
}
