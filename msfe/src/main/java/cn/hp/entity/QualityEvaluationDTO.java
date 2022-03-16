package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityEvaluationDTO {
    private String id;
    private String taskId;
    private String serviceName;
    private Double adaptation;
    private String securityComponent;
    private String selfInvocation;
    private String loadBalanceComponent;
    private String serviceRegistryCenter;
    private String cpa;
}
