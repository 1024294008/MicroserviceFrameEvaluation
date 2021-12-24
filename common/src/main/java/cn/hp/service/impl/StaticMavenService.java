package cn.hp.service.impl;

import cn.hp.entity.Module;
import cn.hp.service.IMavenService;
import cn.hp.service.IPomParseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class StaticMavenService implements IMavenService {
    @Resource
    private IPomParseService pomParseService;

    @Override
    public List<String> resolveUnusedDependencyList(Module module) {
        return new ArrayList<>();
    }

    @Override
    public List<String> resolveTopDependencyList(Module module) {
        return resolveDependencyList(module);
    }

    @Override
    public List<String> resolveDependencyList(Module module) {
        return pomParseService.parseDependencyList(new File(module.getLocation(), "pom.xml"));
    }
}
