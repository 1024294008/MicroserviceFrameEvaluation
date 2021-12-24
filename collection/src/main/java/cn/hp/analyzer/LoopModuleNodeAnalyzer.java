package cn.hp.analyzer;

import cn.hp.entity.ModuleNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoopModuleNodeAnalyzer {
    private List<Integer> trace;
    private List<Integer> searched;
    private List<List<Integer>> circles;

    public List<List<Integer>> detectLoopModuleNode(List<ModuleNode> moduleNodes) {
        searched = new ArrayList<>();
        circles = new ArrayList<>();
        for (int i = 0; i < moduleNodes.size(); i++) {
            if (!searched.contains(i)) {
                trace = new ArrayList<>();
                findCycle(moduleNodes, i);
            }
        }
        return circles;
    }

    private void findCycle(List<ModuleNode> moduleNodes, Integer i) {
        int j = trace.indexOf(i);
        if(j != -1) {
            List<Integer> circle=new ArrayList<>();
            while(j < trace.size()) {
                circle.add(trace.get(j));
                j++;
            }
            circles.add(circle);
            return;
        }
        trace.add(i);
        List<ModuleNode> children = moduleNodes.get(i).getChildren();
        for (ModuleNode child: children) {
            Integer no = searchModuleNodeNo(moduleNodes, child);
            searched.add(no);
            findCycle(moduleNodes, no);
        }
        trace.remove(trace.size() - 1);
    }

    private Integer searchModuleNodeNo(List<ModuleNode> moduleNodes, ModuleNode moduleNode) {
        for (int i = 0; i < moduleNodes.size(); i++) {
            if (moduleNodes.get(i) == moduleNode) {
                return i;
            }
        }
        return -1;
    }
}
