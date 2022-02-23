package cn.hp.availability;

import cn.hp.entity.MicroFrameFeature;
import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AvailabilityEvaluator {
    @Resource
    private LoadBalanceDetector loadBalanceDetector;

    public void evaluateAvailability(MicroFrameFeature microFrameFeature) {
        List<ModuleFeature> moduleFeatures = microFrameFeature.getModuleFeatures();
        for (ModuleFeature moduleFeature: moduleFeatures) {
            System.out.println(loadBalanceDetector.detectLoadBalance(moduleFeature));
        }
    }
}
