package cn.hp.entity;

import cn.hp.bean.ServiceComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompFeature {
    private ServiceComponent serviceComponent;
    private DependencyType dependencyType;
    private List<String> dependenciesStack;
}
