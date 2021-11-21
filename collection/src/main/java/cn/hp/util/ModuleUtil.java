package cn.hp.util;

import cn.hp.entity.DependencyAnalyzeLog;
import cn.hp.entity.Module;

import java.util.ArrayList;
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

    public static DependencyAnalyzeLog obtainTargetDependencyAnalyzeLog(Module module, List<DependencyAnalyzeLog> dependencyAnalyzeLogs) {
        for (DependencyAnalyzeLog dependencyAnalyzeLog: dependencyAnalyzeLogs) {
            if (module.getGroupId().equals(dependencyAnalyzeLog.getGroupId())
                    && module.getArtifactId().equals(dependencyAnalyzeLog.getArtifactId())) {
                return dependencyAnalyzeLog;
            }
        }
        DependencyAnalyzeLog dependencyAnalyzeLog = new DependencyAnalyzeLog();
        dependencyAnalyzeLog.setUnusedDependencies(new ArrayList<>());
        return dependencyAnalyzeLog;
    }
}
