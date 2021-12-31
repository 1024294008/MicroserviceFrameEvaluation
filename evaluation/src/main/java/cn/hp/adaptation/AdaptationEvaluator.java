package cn.hp.adaptation;

import cn.hp.entity.CallGraph;
import cn.hp.entity.MicroFrameFeature;
import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdaptationEvaluator {
    private static final Double DEFAULT_FE_COEFFICIENT = 8.5;
    private static final Double DEFAULT_SCALE_COEFFICIENT = 1.5;

    private Double feCoefficient;
    private Double scaleCoefficient;

    @Resource
    private ApiCalculator apiCalculator;

    @Resource
    private CalledFrequencyCalculator calledFrequencyCalculator;

    public AdaptationEvaluator() {
        this.feCoefficient = DEFAULT_FE_COEFFICIENT;
        this.scaleCoefficient = DEFAULT_SCALE_COEFFICIENT;
    }

    public Double evaluateAdaptation(MicroFrameFeature microFrameFeature) {
        Double adaptation = 0.0;
        List<ModuleFeature> moduleFeatures = microFrameFeature.getModuleFeatures();
        for (ModuleFeature moduleFeature: moduleFeatures) {
            adaptation += evaluateImpact(moduleFeature, microFrameFeature);
        }
        return adaptation;
    }

    public Double evaluateImpact(ModuleFeature currentModuleFeature, MicroFrameFeature microFrameFeature) {
        Integer fe, feTotal = 0, scale, scaleTotal = 0;
        List<ModuleFeature> moduleFeatures = microFrameFeature.getModuleFeatures();
        CallGraph callGraph = microFrameFeature.getCallGraph();

        fe = calledFrequencyCalculator.calculateCalledFrequency(currentModuleFeature, callGraph);
        scale = apiCalculator.calculateApi(currentModuleFeature);

        for (ModuleFeature moduleFeature: moduleFeatures) {
            feTotal += calledFrequencyCalculator.calculateCalledFrequency(moduleFeature, callGraph);
            scaleTotal += apiCalculator.calculateApi(moduleFeature);
        }

        return feCoefficient * ((double)fe / feTotal)
                + scaleCoefficient * ((double)scale / scaleTotal);

    }

    public void setFeCoefficient(Double feCoefficient) {
        this.feCoefficient = feCoefficient;
    }

    public Double getFeCoefficient() {
        return feCoefficient;
    }

    public void setScaleCoefficient(Double scaleCoefficient) {
        this.scaleCoefficient = scaleCoefficient;
    }

    public Double getScaleCoefficient() {
        return scaleCoefficient;
    }
}
