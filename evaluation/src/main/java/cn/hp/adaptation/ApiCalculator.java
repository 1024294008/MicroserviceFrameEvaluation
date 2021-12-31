package cn.hp.adaptation;

import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Service;

@Service
public class ApiCalculator {
    public Integer calculateApi(ModuleFeature moduleFeature) {
        return moduleFeature.getInterfaceFeatures().size();
    }
}
