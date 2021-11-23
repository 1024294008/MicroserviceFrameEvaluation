package cn.hp.resolver;

import cn.hp.entity.*;
import cn.hp.service.IMavenService;
import cn.hp.util.ModuleUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DependencyResolver {
    @Resource
    private IMavenService mavenService;

    public DependencyFeature resolveDependency(ModuleNode moduleNode, List<ModuleFeature> moduleFeatures) {
        Map<Module, ModuleFeature> moduleModuleFeatureMap = new HashMap<>();

        for (ModuleFeature moduleFeature: moduleFeatures) {
            moduleModuleFeatureMap.put(moduleFeature.getModule(), moduleFeature);
        }

        DependencyFeature dependencyFeature = new DependencyFeature(
                moduleNode.getModule().getGroupId() + ":"
                + moduleNode.getModule().getArtifactId() + ":",
                DependencyType.UsedModule,
                new ArrayList<>()
        );
        traverseModuleNode(moduleNode, dependencyFeature, moduleModuleFeatureMap, true);
        return dependencyFeature;
    }

    private void traverseModuleNode(ModuleNode moduleNode, DependencyFeature parent, Map<Module, ModuleFeature> moduleModuleFeatureMap, Boolean usedFlag) {
        if (null == moduleNode) return;
        Module module = moduleNode.getModule();

        List<String> unusedDependencyList = ModuleUtil.obtainTargetDependencyAnalyzeLog(module, mavenService.resolveUnusedDependencies(module)).getUnusedDependencies();
        unusedDependencyList = unusedDependencyList.stream().map(unusedDependency -> {
            String[] sections = unusedDependency.split(":");
            if (sections.length > 2) {
                return sections[0] + ":" + sections[1];
            }
            return "";
        }).collect(Collectors.toList());

        if (usedFlag) parent.getChildren().addAll(moduleModuleFeatureMap.get(module).getDependencyFeature());
        else {
            List<DependencyFeature> dependencyFeatures = moduleModuleFeatureMap.get(module).getDependencyFeature();
            for (DependencyFeature dependencyFeature: dependencyFeatures) {
                parent.getChildren().add(new DependencyFeature(
                        dependencyFeature.getValue(),
                        DependencyType.UnusedJar,
                        new ArrayList<>()
                ));
            }
        }

        List<ModuleNode> subModuleNodes = moduleNode.getChildren();
        for (ModuleNode subModuleNode: subModuleNodes) {
            DependencyFeature dependencyFeature = new DependencyFeature();
            dependencyFeature.setValue(subModuleNode.getModule().getGroupId() + ":" + subModuleNode.getModule().getArtifactId());
            dependencyFeature.setChildren(new ArrayList<>());
            parent.getChildren().add(dependencyFeature);
            if (unusedDependencyList.contains(subModuleNode.getModule().getGroupId() + ":" + subModuleNode.getModule().getArtifactId())) {
                dependencyFeature.setType(DependencyType.UnusedModule);
                traverseModuleNode(subModuleNode, dependencyFeature, moduleModuleFeatureMap, false);
            } else {
                dependencyFeature.setType(DependencyType.UsedModule);
                traverseModuleNode(subModuleNode, dependencyFeature, moduleModuleFeatureMap, true);
            }
        }
    }
}
