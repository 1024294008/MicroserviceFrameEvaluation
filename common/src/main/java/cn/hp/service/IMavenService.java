package cn.hp.service;

import cn.hp.entity.DependencyLog;
import cn.hp.entity.Module;

import java.util.List;

public interface IMavenService {
    List<DependencyLog> resolveDependencyTree(Module module);

    List<DependencyLog> resolveDependencyTreeIncludes(Module module, String packageName);

    List<String> resolveUnusedDependencies(Module module);
}
