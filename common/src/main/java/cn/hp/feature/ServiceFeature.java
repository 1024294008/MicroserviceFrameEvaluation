package cn.hp.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceFeature {
    private String name;
    private String port;
    private String context;
    private String registryUrl;
    private File location;
}
