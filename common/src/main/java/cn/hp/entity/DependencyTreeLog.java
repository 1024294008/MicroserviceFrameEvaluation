package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependencyLog {
    private String groupId;
    private String artifactId;
    private DependencyTreeNode dependencyTreeNode;
}
