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
    private ServiceFeature serviceFeature;
    private List<DependencyFeature> dependencyFeature;
}
