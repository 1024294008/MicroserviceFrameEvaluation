package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependencyGraphEdge {
    private String source;
    private String target;
    private DependencyGraphEdgeType type;
}
