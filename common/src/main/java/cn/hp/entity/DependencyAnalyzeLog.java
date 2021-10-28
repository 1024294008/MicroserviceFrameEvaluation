package cn.hp.entity;

import lombok.Data;

import java.util.List;

@Data
public class DependencyAnalyzeLog {
    private String groupId;
    private String artifactId;
    private List<String> unusedDependencies;
}
