package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MicroServiceDTO {
    private String id;
    private String taskId;
    private String moduleName;
    private String entryClass;
    private String serviceName;
    private String port;
    private String context;
    private String registryUrl;
}
