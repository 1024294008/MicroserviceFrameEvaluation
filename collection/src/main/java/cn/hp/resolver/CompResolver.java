package cn.hp.resolver;

import cn.hp.bean.ServiceComponent;
import cn.hp.bean.ServiceComponentRegistry;
import cn.hp.entity.*;
import cn.hp.service.IMavenService;
import cn.hp.util.ModuleUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CompResolver {
    private Map<String, ServiceComponent> serviceComponentRegistryMap;

    @Resource
    private ServiceComponentRegistry serviceComponentRegistry;

    @Resource
    private IMavenService mavenService;

    public List<CompFeature> resolveComp(Module module) {
        if (null == serviceComponentRegistryMap) serviceComponentRegistryMap = obtainServiceComponentRegistryMap();
        List<CompFeature> compFeatures = new ArrayList<>();
        List<DependencyTreeLog> dependencyTreeLogs = mavenService.resolveDependencyTree(module);
        List<DependencyAnalyzeLog> dependencyAnalyzeLogs = mavenService.resolveUnusedDependencies(module);
        List<String> unusedDependencies = ModuleUtil.obtainTargetDependencyAnalyzeLog(module, dependencyAnalyzeLogs).getUnusedDependencies();

        for (DependencyTreeLog dependencyTreeLog: dependencyTreeLogs) {
            if (dependencyTreeLog.getGroupId().equalsIgnoreCase(module.getGroupId()) && dependencyTreeLog.getArtifactId().equalsIgnoreCase(module.getArtifactId())) {
                traverseTreeNode(dependencyTreeLog.getDependencyTreeNode(), new Stack<>(), compFeatures, unusedDependencies, false);
            }
        }
        return compFeatures;
    }

    private Map<String, ServiceComponent> obtainServiceComponentRegistryMap() {
        List<ServiceComponent> serviceComponents = serviceComponentRegistry.getServiceComponents();
        Map<String, ServiceComponent> serviceComponentRegistryMap = new HashMap<>();
        for (ServiceComponent serviceComponent: serviceComponents) {
            String packageName;
            if (null == serviceComponent.getVersion() || serviceComponent.getVersion().equalsIgnoreCase("x"))
                packageName = serviceComponent.getGroupId() + ":" + serviceComponent.getArtifactId();
            else
                packageName = serviceComponent.getGroupId() + ":" + serviceComponent.getArtifactId() + ":" + serviceComponent.getVersion();
            serviceComponentRegistryMap.put(packageName, serviceComponent);
        }
        return serviceComponentRegistryMap;
    }

    private void traverseTreeNode(
            DependencyTreeNode dependencyTreeNode,
            Stack<String> stack,
            List<CompFeature> compFeatures,
            List<String> unusedDependencies,
            Boolean unusedFlag
    ) {
        if (null == dependencyTreeNode) return;
        String rawPackageName = dependencyTreeNode.getPackageName();
        if (null == rawPackageName) return;
        if (unusedDependencies.contains(rawPackageName)) unusedFlag = true;
        stack.push(rawPackageName);
        String[] rawPackageSections = rawPackageName.split(":");
        if (rawPackageSections.length >= 4) {
            String packageName = rawPackageSections[0] + ":" + rawPackageSections[1];
            if (!serviceComponentRegistryMap.containsKey(packageName))
                packageName = rawPackageSections[0] + ":" + rawPackageSections[1] + ":" + rawPackageSections[3];
            if (serviceComponentRegistryMap.containsKey(packageName)) {
                compFeatures.add(new CompFeature(serviceComponentRegistryMap.get(packageName), unusedFlag ? DependencyType.UnusedJar : DependencyType.UsedJar,new ArrayList<>(stack)));
            }
        }
        List<DependencyTreeNode> children = dependencyTreeNode.getChildren();
        if (null != children && 0 < children.size()) {
            for (DependencyTreeNode child: children) {
                traverseTreeNode(child, stack, compFeatures, unusedDependencies, unusedFlag);
            }
        }
        stack.pop();
    }
}
