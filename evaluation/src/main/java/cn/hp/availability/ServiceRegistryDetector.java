package cn.hp.availability;

import cn.hp.bean.ServiceComponent;
import cn.hp.entity.DependencyFeature;
import cn.hp.entity.MicroFrameFeature;
import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRegistryDetector {
    private List<String> serviceRegistryTags;

    public ServiceRegistryDetector() {
        serviceRegistryTags = new ArrayList<>();
        serviceRegistryTags.add("eureka-server");
        serviceRegistryTags.add("eureka-client");
        serviceRegistryTags.add("consul");
        serviceRegistryTags.add("zookeeper");
    }

    public String detectServiceRegistry(MicroFrameFeature microFrameFeature) {
        List<ModuleFeature> moduleFeatures = microFrameFeature.getModuleFeatures();
        for (ModuleFeature moduleFeature: moduleFeatures) {
            List<DependencyFeature> dependencyFeatures = moduleFeature.getDependencyFeature();
            for (DependencyFeature dependencyFeature: dependencyFeatures) {
                ServiceComponent serviceComponent = dependencyFeature.getServiceComponent();
                if (serviceComponent.getType().equals("SERVICE_RAD")
                        && serviceRegistryTags.contains(serviceComponent.getTag())) {
                    return serviceComponent.getType();
                }
            }
        }
        return null;
    }
}
