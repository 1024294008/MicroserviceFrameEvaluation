package cn.hp.util;


import cn.hp.feature.ServiceFeature;

public class MergeUtil {
    public static ServiceFeature mergeServiceFeature(ServiceFeature serviceFeatureA, ServiceFeature serviceFeatureB) {
        if (null == serviceFeatureA && null == serviceFeatureB) return null;
        ServiceFeature serviceFeatureAaB = new ServiceFeature();
        mergeSingleServiceFeature(serviceFeatureAaB, serviceFeatureA);
        mergeSingleServiceFeature(serviceFeatureAaB, serviceFeatureB);
        return serviceFeatureAaB;
    }

    private static void mergeSingleServiceFeature (ServiceFeature baseFeature, ServiceFeature serviceFeature) {
        if (null != serviceFeature) {
            if (null != serviceFeature.getName() && !serviceFeature.getName().trim().equals(""))
                baseFeature.setName(serviceFeature.getName());
            if (null != serviceFeature.getPort() && !serviceFeature.getPort().trim().equals(""))
                baseFeature.setPort(serviceFeature.getPort());
            if (null != serviceFeature.getContext() && !serviceFeature.getContext().trim().equals(""))
                baseFeature.setContext(serviceFeature.getContext());
            if (null != serviceFeature.getRegistryUrl() && !serviceFeature.getRegistryUrl().trim().equals(""))
                baseFeature.setRegistryUrl(serviceFeature.getRegistryUrl());
        }
    }
}
