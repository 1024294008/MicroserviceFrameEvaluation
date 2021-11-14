package cn.hp.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@PropertySource(value = "service-component-registry.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "service-component-registry")
public class ServiceComponentRegistry {
    private List<ServiceComponent> serviceComponents;
}
