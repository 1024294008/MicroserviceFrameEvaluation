package cn.hp.security;

import cn.hp.entity.CallGraph;
import cn.hp.entity.CallGraphEdge;
import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SelfInvocationDetector {
    public List<String> detectSelfInvocation(ModuleFeature moduleFeature, CallGraph callGraph) {
        List<String> selfInvocation = new ArrayList<>();
        List<CallGraphEdge> edges = callGraph.getEdges();
        for (CallGraphEdge edge: edges) {
            if (edge.getSourceService().equals(moduleFeature.getServiceFeature().getName())
                    && edge.getSourceService().equals(edge.getTargetService()))
                selfInvocation.add(edge.getApiInfo());
        }
        return selfInvocation;
    }
}
