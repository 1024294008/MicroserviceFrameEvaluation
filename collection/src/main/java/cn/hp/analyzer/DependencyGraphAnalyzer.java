package cn.hp.analyzer;

import cn.hp.entity.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DependencyGraphAnalyzer {
    public DependencyGraph resolveDependencyGraph(List<ModuleNode> moduleNodes, List<List<Integer>> circles) {
        DependencyGraph dependencyGraph = new DependencyGraph();
        dependencyGraph.setNodes(resolveDependencyGraphNode(moduleNodes));
        dependencyGraph.setEdges(resolveDependencyGraphEdge(moduleNodes, circles));
        return dependencyGraph;
    }

    private List<DependencyGraphNode> resolveDependencyGraphNode(List<ModuleNode> moduleNodes) {
        Set<String> packageNameSet = new HashSet<>();
        for (ModuleNode moduleNode: moduleNodes) {
            packageNameSet.add(moduleNode.getModule().getGroupId() + ":" + moduleNode.getModule().getArtifactId());
        }
        List<String> packageNames = new ArrayList<>(packageNameSet);
        List<DependencyGraphNode> nodes = new ArrayList<>();
        for (String packageName: packageNames) {
            nodes.add(new DependencyGraphNode(packageName, DependencyType.UsedModule));
        }
        return nodes;
    }

    private List<DependencyGraphEdge> resolveDependencyGraphEdge(List<ModuleNode> moduleNodes, List<List<Integer>> circles) {
        Map<String, DependencyGraphEdge> dependencyGraphEdgeMap = new HashMap<>();
        for (ModuleNode moduleNode: moduleNodes) {
            Module moduleA = moduleNode.getModule();
            List<ModuleNode> children = moduleNode.getChildren();
            for (ModuleNode child: children) {
                Module moduleB = child.getModule();
                String packageNameA = moduleA.getGroupId() + ":" + moduleA.getArtifactId();
                String packageNameB = moduleB.getGroupId() + ":" + moduleB.getArtifactId();
                dependencyGraphEdgeMap.put(packageNameA + " -> " + packageNameB,
                        new DependencyGraphEdge(packageNameA, packageNameB, DependencyGraphEdgeType.Normal));
            }
        }
        for (List<Integer> circle: circles) {
            if (1 != circle.size()) {
                for (int i = 0; i < circle.size() - 1; i++) {
                    Module moduleA = moduleNodes.get(circle.get(i)).getModule();
                    Module moduleB = moduleNodes.get(circle.get(i + 1)).getModule();
                    String packageNameA = moduleA.getGroupId() + ":" + moduleA.getArtifactId();
                    String packageNameB = moduleB.getGroupId() + ":" + moduleB.getArtifactId();
                    dependencyGraphEdgeMap.put(packageNameA + " -> " + packageNameB,
                            new DependencyGraphEdge(packageNameA, packageNameB, DependencyGraphEdgeType.Ring));
                }
            }
            Module moduleA = moduleNodes.get(circle.get(circle.size() - 1)).getModule();
            Module moduleB = moduleNodes.get(circle.get(0)).getModule();
            String packageNameA = moduleA.getGroupId() + ":" + moduleA.getArtifactId();
            String packageNameB = moduleB.getGroupId() + ":" + moduleB.getArtifactId();
            dependencyGraphEdgeMap.put(packageNameA + " -> " + packageNameB,
                    new DependencyGraphEdge(packageNameA, packageNameB, DependencyGraphEdgeType.Ring));
        }
        return new ArrayList<>(dependencyGraphEdgeMap.values());
    }
}
