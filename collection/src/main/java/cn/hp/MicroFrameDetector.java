package cn.hp;

import cn.hp.analyzer.DependencyAnalyzer;
import cn.hp.analyzer.DependencyGraphAnalyzer;
import cn.hp.analyzer.LoopModuleNodeAnalyzer;
import cn.hp.bean.ProjectInfo;
import cn.hp.entity.*;
import cn.hp.resolver.MicroServiceResolver;
import cn.hp.resolver.ModuleRelationResolver;
import cn.hp.resolver.ModuleResolver;
import cn.hp.resolver.ModuleScanner;
import cn.hp.service.IMavenService;
import cn.hp.util.MicroServiceExecuteLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MicroFrameDetector {
    @Resource
    private ModuleScanner moduleScanner;

    @Resource
    private ModuleRelationResolver moduleRelationResolver;

    @Resource
    private MicroServiceResolver microServiceResolver;

    @Resource
    private ModuleResolver moduleResolver;

    @Resource
    private DependencyAnalyzer dependencyAnalyzer;

    @Resource
    private LoopModuleNodeAnalyzer loopModuleNodeAnalyzer;

    @Resource
    private DependencyGraphAnalyzer dependencyGraphAnalyzer;

//    @Resource
//    private CallGraphResolver callGraphResolver;

    @Resource
    private ProjectInfo projectInfo;

    public MicroFrameFeature getMicroFrameFeature(File root) {
        MicroServiceExecuteLog.info("Start detecting source code...");

        MicroFrameFeature microFrameFeature = new MicroFrameFeature();
        List<ModuleFeature> moduleFeatures = new ArrayList<>();
        DependencyRelation dependencyRelation = new DependencyRelation();
        microFrameFeature.setProjectFile(root);

        MicroServiceExecuteLog.info("Generate the file graph.");
        projectInfo.setProjectFile(root);

        MicroServiceExecuteLog.info("Extract microservice features.");
        List<Module> moduleList = moduleResolver.resolveModule(root);
        for (Module module: moduleList) {
            if (module.getPackageType().equals(PackageType.Jar)) {
                ModuleFeature moduleFeature = moduleScanner.scanModule(module);
                moduleFeatures.add(moduleFeature);
            }
        }
        List<ModuleFeature> microServiceFeatures = microServiceResolver.resolveMicroService(moduleFeatures);
        if (microServiceFeatures.size() == 0) {
            microFrameFeature.setModuleFeatures(microServiceFeatures);
            return microFrameFeature;
        }

        List<ModuleNode> moduleNodes = moduleRelationResolver.resolveModuleRelation(moduleList);
        List<List<Integer>> circles = loopModuleNodeAnalyzer.detectLoopModuleNode(moduleNodes);
        if (circles.size() > 0) {
            DependencyGraph dependencyGraph = dependencyGraphAnalyzer.resolveDependencyGraph(moduleNodes, circles);
            dependencyRelation.setType(DependencyRelationType.Graph);
            dependencyRelation.setDependencyGraph(dependencyGraph);
        } else {
            List<DependencyFeature> dependencyFeatures = new ArrayList<>();

            Map<Module, ModuleNode> moduleModuleNodeMap = new HashMap<>();
            for (ModuleNode moduleNode: moduleNodes) {
                moduleModuleNodeMap.put(moduleNode.getModule(), moduleNode);
            }
            for (ModuleFeature microServiceFeature: microServiceFeatures) {
                DependencyFeature dependencyFeature = dependencyAnalyzer.resolveDependency(moduleModuleNodeMap.get(microServiceFeature.getModule()), moduleFeatures);
                dependencyFeatures.add(dependencyFeature);
            }

            DependencyFeature dependencyFeature = new DependencyFeature(root.getName(), DependencyType.Root, dependencyFeatures);
            dependencyRelation.setType(DependencyRelationType.Tree);
            dependencyRelation.setDependencyFeature(dependencyFeature);
        }

        microFrameFeature.setModuleFeatures(microServiceFeatures);
        microFrameFeature.setDependencyRelation(dependencyRelation);
//        microFrameFeature.setCallGraph(callGraphResolver.resolveCallGraph(microServiceFeatures));

        return microFrameFeature;
    }
}
