package cn.hp.security;

import cn.hp.bean.ServiceComponent;
import cn.hp.entity.DependencyFeature;
import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SecurityComponentDetector {
    private List<String> securityComponentTags;

    public SecurityComponentDetector() {
        securityComponentTags = new ArrayList<>();
        securityComponentTags.add("oauth2");
        securityComponentTags.add("spring-security");
    }

    public List<String> detectSecurityComponent(ModuleFeature moduleFeature) {
        List<DependencyFeature> dependencyFeatures = moduleFeature.getDependencyFeature();
        Set<String> securityComponentSet = new HashSet<>();

        for (DependencyFeature dependencyFeature: dependencyFeatures) {
            ServiceComponent serviceComponent = dependencyFeature.getServiceComponent();
            if (securityComponentTags.contains(serviceComponent.getTag())) {
                securityComponentSet.add(serviceComponent.getDescription());
            }
        }
        return new ArrayList<>(securityComponentSet);
    }
}
