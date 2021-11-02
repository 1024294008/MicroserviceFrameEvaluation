package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependencyAnalyzeLog {
    private String groupId;
    private String artifactId;
    private List<String> unusedDependencies;
}
