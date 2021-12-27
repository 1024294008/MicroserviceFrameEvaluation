package cn.hp.resolver;

import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MicroServiceResolver {
    public List<ModuleFeature> resolveMicroService(List<ModuleFeature> moduleFeatures) {
        List<ModuleFeature> microServiceFeatures = new ArrayList<>();
        for (ModuleFeature moduleFeature: moduleFeatures) {
            if (null != moduleFeature.getServiceFeature()
                    && null != moduleFeature.getServiceFeature().getName()
                    && null != moduleFeature.getCodeFeature())
                microServiceFeatures.add(moduleFeature);
        }
        return microServiceFeatures;
    }
}
