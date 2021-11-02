package cn.hp.util;

import cn.hp.entity.Module;

import java.util.List;

public class ModuleUtil {
    public static Module searchTopModule(List<Module> modules) {
        Module topModule = null;
        Integer pathLength = Integer.MAX_VALUE;
        for (Module module: modules) {
            Integer modulePathLength = module.getLocation().getAbsoluteFile().getAbsolutePath().length();
            if (pathLength > modulePathLength) {
                topModule = module;
                pathLength = modulePathLength;
            }
        }
        return topModule;
    }
}
