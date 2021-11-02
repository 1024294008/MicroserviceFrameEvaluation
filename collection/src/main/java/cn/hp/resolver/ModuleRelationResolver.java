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

@Service
public class ModuleRelationResolver {
    @Resource
    private IMavenService mavenService;

    public List<ModuleNode> resolveModuleRelation(List<Module> modules) {
        List<ModuleNode> moduleNodes = new ArrayList<>();
        Module topModule = ModuleUtil.searchTopModule(modules);

        List<DependencyTreeLog> dependencyTreeLogs = mavenService.resolveDependencyTree(topModule);
        List<DependencyAnalyzeLog> dependencyAnalyzeLogs = mavenService.resolveUnusedDependencies(topModule);
        Map<String, DependencyAnalyzeLog> dependencyAnalyzeLogMap = new HashMap<>();
        for (DependencyAnalyzeLog dependencyAnalyzeLog: dependencyAnalyzeLogs) {
            dependencyAnalyzeLogMap.put(
                    dependencyAnalyzeLog.getGroupId() + ":" + dependencyAnalyzeLog.getArtifactId(),
                    dependencyAnalyzeLog
            );
        }

        Map<String, ModuleNode> moduleNodeMap = new HashMap<>();

        for (Module module: modules) {
            if (module.getPackageType().equals(PackageType.Jar)) {
                ModuleNode moduleNode = new ModuleNode(module);
                moduleNodes.add(moduleNode);
                moduleNodeMap.put(module.getGroupId() + ":" + module.getArtifactId(), moduleNode);
            }
        }
        for (DependencyTreeLog dependencyTreeLog: dependencyTreeLogs) {
            String packageName = dependencyTreeLog.getGroupId() + ":" + dependencyTreeLog.getArtifactId();
            if (moduleNodeMap.containsKey(packageName)) {
                ModuleNode moduleNode = moduleNodeMap.get(packageName);
                Module module = moduleNode.getModule();

                if (dependencyAnalyzeLogMap.containsKey(module.getGroupId() + ":" + module.getArtifactId())) {
                    System.out.println(module.getArtifactId());
                    moduleNode.setChildren(extractChildren(
                            dependencyTreeLog,
                            dependencyAnalyzeLogMap.get(module.getGroupId() + ":" + module.getArtifactId()),
                            moduleNodeMap)
                    );
                } else {
                    moduleNode.setChildren(extractChildren(dependencyTreeLog, new DependencyAnalyzeLog(null, null, new ArrayList<>()) ,moduleNodeMap));
                }
            }
        }
        return moduleNodes;
    }

    private List<ModuleNode> extractChildren(
            DependencyTreeLog dependencyTreeLog,
            DependencyAnalyzeLog dependencyAnalyzeLog,
            Map<String, ModuleNode> moduleNodeMap
    ) {
        List<ModuleNode> moduleNodes = new ArrayList<>();
        List<DependencyTreeNode> children = dependencyTreeLog.getDependencyTreeNode().getChildren();

        List<String> unusedDependencies = dependencyAnalyzeLog.getUnusedDependencies();
        Map<String, Boolean> unusedDependencyMap = new HashMap<>();
        for (String unusedDependency: unusedDependencies) {
            unusedDependencyMap.put(unusedDependency, true);
        }

        for (DependencyTreeNode dependencyTreeNode: children) {
            String[] sections = dependencyTreeNode.getPackageName().split(":");
            System.out.println(dependencyTreeNode.getPackageName());
            System.out.println(unusedDependencyMap.keySet());
            if (sections.length >= 2 && !unusedDependencyMap.containsKey(dependencyTreeNode.getPackageName()) && moduleNodeMap.containsKey(sections[0] + ":" + sections[1])) {
                moduleNodes.add(moduleNodeMap.get(sections[0] + ":" + sections[1]));
            }
        }
        return moduleNodes;
    }
}
