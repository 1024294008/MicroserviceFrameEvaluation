package cn.hp.availability;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CpaEvaluator {
    private Map<String, String> cpaMap;

    public CpaEvaluator() {
        cpaMap = new HashMap<>();
        cpaMap.put("eureka-server", "AP");
        cpaMap.put("eureka-client", "AP");
        cpaMap.put("eureka", "AP");
        cpaMap.put("consul", "CP");
        cpaMap.put("zookeeper", "CP");
    }

    public String evaluateCpa(String serviceRegistry) {
        return cpaMap.get(serviceRegistry);
    }
}
