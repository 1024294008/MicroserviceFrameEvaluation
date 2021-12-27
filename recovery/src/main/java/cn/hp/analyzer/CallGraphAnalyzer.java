package cn.hp.analyzer;

import cn.hp.entity.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CallGraphAnalyzer {
    public CallGraph resolveCallGraph(List<ModuleFeature> microServiceFeatures) {
        CallGraph callGraph = new CallGraph();
        List<CallGraphNode> callGraphNodes = new ArrayList<>();
        List<CallGraphEdge> callGraphEdges = new ArrayList<>();
        List<String> serviceNameList = new ArrayList<>();

        for (ModuleFeature moduleFeature: microServiceFeatures) {
            serviceNameList.add(moduleFeature.getServiceFeature().getName());
        }

        for (ModuleFeature moduleFeature: microServiceFeatures) {
            List<InterfaceFeature> interfaceFeatures = moduleFeature.getInterfaceFeatures();
            List<CallFeature> callFeatures = moduleFeature.getCallFeatures();
            CallGraphNode callGraphNode = new CallGraphNode();
            List<String> apiList = new ArrayList<>();
            String belongService = moduleFeature.getServiceFeature().getName().toUpperCase();

            callGraphNode.setService(belongService);
            callGraphNode.setApiList(apiList);
            for (InterfaceFeature interfaceFeature: interfaceFeatures) {
                apiList.add(interfaceFeature.getRequestType()
                        + " " + interfaceFeature.getRequestPath());
            }
            callGraphNodes.add(callGraphNode);

            for (CallFeature callFeature: callFeatures) {
                callGraphEdges.add(new CallGraphEdge(callFeature.getService(), belongService, callFeature.getApi()));

                if (!serviceNameList.contains(callFeature.getService())) {
                    serviceNameList.add(callFeature.getService());
                    callGraphNodes.add(new CallGraphNode());
                }
            }
        }

        callGraph.setNodes(callGraphNodes);
        callGraph.setEdges(callGraphEdges);

        return callGraph;
    }
}
