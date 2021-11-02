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
                moduleNode.setChildren(extractChildren(dependencyTreeLog, moduleNodeMap));
            }
        }
        return moduleNodes;
    }

    private List<ModuleNode> extractChildren(DependencyTreeLog dependencyTreeLog, Map<String, ModuleNode> moduleNodeMap) {
        List<ModuleNode> moduleNodes = new ArrayList<>();
        List<DependencyTreeNode> children = dependencyTreeLog.getDependencyTreeNode().getChildren();
        for (DependencyTreeNode dependencyTreeNode: children) {
            String[] sections = dependencyTreeNode.getPackageName().split(":");
            if (sections.length >= 2 && moduleNodeMap.containsKey(sections[0] + ":" + sections[1])) {
                moduleNodes.add(moduleNodeMap.get(sections[0] + ":" + sections[1]));
            }
        }
        return moduleNodes;
    }
}
