package cn.hp.service.impl;

import cn.hp.MicroFrameDetector;
import cn.hp.bean.ServiceComponentRegistry;
import cn.hp.entity.MicroFrameFeature;
import cn.hp.entity.Module;
import cn.hp.entity.ModuleFeature;
import cn.hp.entity.ModuleNode;
import cn.hp.resolver.*;
import cn.hp.service.ITestService;
import cn.hp.util.MicroServiceExecuteLog;
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
    private ASTResolver astResolver;

    @Resource
    private CompResolver compResolver;

    @Resource
    private MicroServiceResolver microServiceResolver;

    @Resource
    private DynamicMavenService dynamicMavenService;

    @Resource
    private MicroFrameDetector microFrameDetector;

    @Resource
    private ServiceComponentRegistry serviceComponentRegistry;

    @Value("D:\\Projects\\dev\\sitech\\reverse-analysis-platform")
    private String projectPath;

    @Override
    public void test() {
//        File file = new File("D:\\Projects\\dev\\microservice-frame-evaluation\\msfe\\src\\main\\java\\cn\\hp\\service\\ITestService.java");
//        System.out.println(astResolver.resolveServerEntry(file));
//        Module module = new Module();
//        module.setLocation(new File(projectPath, "msfe"));
//        module.setGroupId("cn.hp.framedetect");
//        module.setArtifactId("msfe");
//        System.out.println(mavenService.resolveDependencyList(module));
//        List<Module> modules = moduleResolver.resolveModule(new File(projectPath));
//
//        for (Module module: modules) {
//            System.out.println(module);
//        }
//        List<ModuleNode> moduleNodes = moduleRelationResolver.resolveModuleRelation(modules);
//        for (ModuleNode moduleNode: moduleNodes) {
//            System.out.println(moduleNode);
//        }
//        System.out.println(compResolver.resolveComp(module));
//        System.out.println(compResolver.resolveComp(module));
        MicroFrameFeature microFrameFeature = microFrameDetector.getMicroFrameFeature(new File(projectPath));
        System.out.println(microFrameFeature.getDependencyRelation());
        List<ModuleFeature> moduleFeatures = microFrameFeature.getModuleFeatures();
        for (ModuleFeature moduleFeature: moduleFeatures) {
            System.out.println(moduleFeature);
        }
        System.out.println(MicroServiceExecuteLog.getLog());
    }
}
