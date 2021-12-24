package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleFeature {
    private Module module;
    private CodeFeature codeFeature;
    private ServiceFeature serviceFeature;
    private List<InterfaceFeature> interfaceFeatures;
    private List<DependencyFeature> dependencyFeature;
    private List<CallFeature> callFeatures;
}
