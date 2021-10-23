package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module {
    private String groupId;
    private String artifactId;
    private PackageType packageType;
    private File location;
}
