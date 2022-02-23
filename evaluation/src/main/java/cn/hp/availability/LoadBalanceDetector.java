package cn.hp.availability;

import cn.hp.bean.ServiceComponent;
import cn.hp.entity.DependencyFeature;
import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LoadBalanceDetector {
    private List<String> loadBalanceTags;

    public LoadBalanceDetector() {
        loadBalanceTags = new ArrayList<>();
        loadBalanceTags.add("ribbon");
        loadBalanceTags.add("feign");
    }

    public List<String> detectLoadBalance(ModuleFeature moduleFeature) {
        List<DependencyFeature> dependencyFeatures = moduleFeature.getDependencyFeature();
        Set<String> loadBalanceSet = new HashSet<>();

        for (DependencyFeature dependencyFeature: dependencyFeatures) {
            ServiceComponent serviceComponent = dependencyFeature.getServiceComponent();
            if (loadBalanceTags.contains(serviceComponent.getTag())) {
                loadBalanceSet.add(serviceComponent.getDescription());
            }
        }
        return new ArrayList<>(loadBalanceSet);
    }
}
