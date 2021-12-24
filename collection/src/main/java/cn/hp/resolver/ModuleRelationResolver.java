package cn.hp.resolver;

import cn.hp.entity.*;
import cn.hp.service.IMavenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleRelationResolver {
    @Resource(name = "dynamicMavenService")
    private IMavenService mavenService;

    public List<ModuleNode> resolveModuleRelation(List<Module> modules) {
        List<ModuleNode> moduleNodes = new ArrayList<>();
        Map<String, ModuleNode> moduleNodeMap = new HashMap<>();

        for (Module module: modules) {
            if (module.getPackageType().equals(PackageType.Jar)) {
                ModuleNode moduleNode = new ModuleNode(module);
                moduleNodes.add(moduleNode);
                moduleNodeMap.put(module.getGroupId() + ":" + module.getArtifactId(), moduleNode);
            }
        }

        for (ModuleNode moduleNode: moduleNodes) {
            moduleNode.setChildren(extractChildren(moduleNode.getModule(), moduleNodeMap));
        }

        return moduleNodes;
    }

    private List<ModuleNode> extractChildren(Module module, Map<String, ModuleNode> moduleNodeMap) {
        List<ModuleNode> moduleNodes = new ArrayList<>();
        List<String> dependencyList = mavenService.resolveTopDependencyList(module);

        for (String dependency: dependencyList) {
            String[] sections = dependency.split(":");
            if (sections.length >= 2 && moduleNodeMap.containsKey(sections[0] + ":" + sections[1])) {
                moduleNodes.add(moduleNodeMap.get(sections[0] + ":" + sections[1]));
            }
        }
        return moduleNodes;
    }
}
