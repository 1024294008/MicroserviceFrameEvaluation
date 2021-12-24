package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MicroFrameFeature {
    private File projectFile;
    private List<ModuleFeature> moduleFeatures;
    private DependencyRelation dependencyRelation;
//    private CallGraph callGraph;
}
