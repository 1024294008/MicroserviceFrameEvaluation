package cn.hp.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceComponent {
    private String type;
    private String groupId;
    private String artifactId;
    private String version;
    private String description;
    private String tag;
}
