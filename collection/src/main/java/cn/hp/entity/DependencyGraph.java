package cn.hp.entity;

import lombok.Data;

import java.util.List;

@Data
public class DependencyGraph {
    private List<DependencyGraphNode> nodes;
    private List<DependencyGraphEdge> edges;
}
