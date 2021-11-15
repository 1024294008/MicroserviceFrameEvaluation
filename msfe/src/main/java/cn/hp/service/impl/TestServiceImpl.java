package cn.hp.service.impl;

import cn.hp.bean.ServiceComponentRegistry;
import cn.hp.entity.Module;
import cn.hp.entity.ModuleNode;
import cn.hp.resolver.CompResolver;
import cn.hp.resolver.ModuleRelationResolver;
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

    @Resource
    private ModuleRelationResolver moduleRelationResolver;

    @Resource
    private CompResolver compResolver;

    @Resource
    private MavenService mavenService;

    @Resource
    private ServiceComponentRegistry serviceComponentRegistry;

    @Value("D:\\Projects\\dev\\microservice-frame-evaluation")
    private String projectPath;

    @Override
    public void test() {
        Module module = new Module();
        module.setLocation(new File(projectPath));
        module.setGroupId("cn.hp.framedetect");
        module.setArtifactId("msfe");
//        mavenService.resolveDependencyTreeIncludes(module, "cn.hp.framedetect:collection");
//        System.out.println(mavenService.resolveUnusedDependencies(module));
//        List<Module> modules = moduleResolver.resolveModule(new File(projectPath));
//        List<ModuleNode> moduleNodes = moduleRelationResolver.resolveModuleRelation(modules);
//        for (ModuleNode moduleNode: moduleNodes) {
//            System.out.println(moduleNode);
//        }
        System.out.println(compResolver.resolveComp(module));
    }

}
