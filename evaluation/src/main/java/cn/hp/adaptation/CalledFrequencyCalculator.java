package cn.hp.adaptation;

import cn.hp.entity.CallGraph;
import cn.hp.entity.CallGraphEdge;
import cn.hp.entity.ModuleFeature;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalledFrequencyCalculator {
    public Integer calculateCalledFrequency(ModuleFeature moduleFeature, CallGraph callGraph) {
        Integer callerFrequency = 0;
        String serviceName = moduleFeature.getServiceFeature().getName();
        List<CallGraphEdge> edges = callGraph.getEdges();
        for (CallGraphEdge edge: edges) {
            if (edge.getSourceService().equals(serviceName))
                callerFrequency++;
        }
        return callerFrequency;
    }
}
