package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallGraphEdge {
    private String sourceService;
    private String targetService;
    private String apiInfo;
}
