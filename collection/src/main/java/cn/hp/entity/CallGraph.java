package cn.hp.entity;

import lombok.Data;

import java.util.List;

@Data
public class CallGraph {
    private List<CallGraphNode> nodes;
    private List<CallGraphEdge> edges;
}
