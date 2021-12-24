package cn.hp.service;

import cn.hp.entity.Module;

import java.util.List;

public interface IMavenService {

    List<String> resolveDependencyList(Module module);

    List<String> resolveTopDependencyList(Module module);

    List<String> resolveUnusedDependencyList(Module module);

}
