package cn.hp.entity;

import lombok.Data;

@Data
public class DependencyRelation {
    private DependencyRelationType type;
    private DependencyFeature dependencyFeature;
    private DependencyGraph dependencyGraph;
}
