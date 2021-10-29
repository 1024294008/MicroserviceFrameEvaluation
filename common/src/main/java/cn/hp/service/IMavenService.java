package cn.hp.service;

import cn.hp.entity.DependencyAnalyzeLog;
import cn.hp.entity.DependencyTreeLog;
import cn.hp.entity.Module;

import java.util.List;

public interface IMavenService {
    List<DependencyTreeLog> resolveDependencyTree(Module module);

    List<DependencyTreeLog> resolveDependencyTreeIncludes(Module module, String packageName);

    List<DependencyAnalyzeLog> resolveUnusedDependencies(Module module);
}
