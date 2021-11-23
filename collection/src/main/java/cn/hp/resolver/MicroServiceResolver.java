package cn.hp.resolver;

import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class MicroServiceResolver {
    public List<ModuleFeature> resolveMicroService(List<ModuleFeature> moduleFeatures) {
        List<ModuleFeature> microServiceFeatures = new ArrayList<>();
        for (ModuleFeature moduleFeature: moduleFeatures) {
            if (null != moduleFeature.getServiceFeature())
                microServiceFeatures.add(moduleFeature);
        }
        return microServiceFeatures;
    }
}
