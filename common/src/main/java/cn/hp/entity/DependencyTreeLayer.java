package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependencyTreeLayer {
    private Integer layer;
    private String packageName;
}
