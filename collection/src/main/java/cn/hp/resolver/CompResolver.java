package cn.hp.resolver;

import cn.hp.bean.ServiceComponent;
import cn.hp.bean.ServiceComponentRegistry;
import cn.hp.entity.*;
import cn.hp.service.IMavenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CompResolver {
    private Map<String, ServiceComponent> serviceComponentRegistryMap;

    @Resource
    private ServiceComponentRegistry serviceComponentRegistry;

    @Resource(name = "staticMavenService")
    private IMavenService mavenService;

    public List<DependencyFeature> resolveComp(Module module) {
        if (null == serviceComponentRegistryMap) serviceComponentRegistryMap = obtainServiceComponentRegistryMap();
        List<DependencyFeature> dependencyFeatures = new ArrayList<>();
        List<String> dependencyList = mavenService.resolveDependencyList(module);
        List<String> unusedDependencyList = mavenService.resolveDependencyList(module);

        Map<String, String>  unusedDependencyMap = new HashMap<>();
        for (String unusedDependency: unusedDependencyList) {
            String[] rawPackageSections = unusedDependency.trim().split(":");
            if (rawPackageSections.length >= 4) {
                unusedDependencyMap.put(rawPackageSections[0] + ":" + rawPackageSections[1], unusedDependency);
                unusedDependencyMap.put(rawPackageSections[0] + ":" + rawPackageSections[1] + ":" + rawPackageSections[3], unusedDependency);
            }
        }

        for (String dependency: dependencyList) {
            DependencyFeature dependencyFeature = resolveNode(dependency, unusedDependencyMap);
            if (null != dependencyFeature) dependencyFeatures.add(dependencyFeature);
        }
        return dependencyFeatures;
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

    private DependencyFeature resolveNode(String dependency, Map<String, String> unusedDependencyMap) {
        DependencyFeature dependencyFeature = null;
        String[] rawPackageSections = dependency.split(":");
        if (rawPackageSections.length >= 2) {
            String packageName = rawPackageSections[0] + ":" + rawPackageSections[1];
            if (!serviceComponentRegistryMap.containsKey(packageName) && rawPackageSections.length >= 3)
                packageName = rawPackageSections[0] + ":" + rawPackageSections[1] + ":" + rawPackageSections[2];
            if (serviceComponentRegistryMap.containsKey(packageName)) {
                dependencyFeature = new DependencyFeature();
                dependencyFeature.setValue(dependency);
                if (unusedDependencyMap.containsKey(packageName))
                    dependencyFeature.setType(DependencyType.UnusedJar);
                else dependencyFeature.setType(DependencyType.UsedJar);
                dependencyFeature.setChildren(new ArrayList<>());
            }
        }
        return dependencyFeature;
    }
}
