package cn.hp.util;

import java.io.File;

public class FileUtil {
    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return 是否删除成功
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String file: children) {
                    boolean success = deleteDir(new File(dir, file));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }
}
