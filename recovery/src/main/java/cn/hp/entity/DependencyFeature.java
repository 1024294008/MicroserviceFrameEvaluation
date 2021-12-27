package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependencyFeature {
    private String value;
    private DependencyType type;
    private List<DependencyFeature> children;
}
