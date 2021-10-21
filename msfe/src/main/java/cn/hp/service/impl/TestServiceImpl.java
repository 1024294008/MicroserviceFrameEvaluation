package cn.hp.service.impl;

import cn.hp.entity.Module;
import cn.hp.resolver.ModuleResolver;
import cn.hp.service.ITestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service
public class TestServiceImpl implements ITestService {
    @Resource
    private ModuleResolver moduleResolver;

    @Value("D:\\Projects\\dev\\microservice-frame-evaluation")
    private String projectPath;

    @Override
    public void test() {
        List<Module> modules = moduleResolver.resolveModule(new File(projectPath));
        for (Module module : modules) {
            System.out.println(module);
        }
    }
}
