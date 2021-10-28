package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DependencyTreeNode {
    private String packageName;
    private List<DependencyTreeNode> children;

    public DependencyTreeNode(String packageName) {
        this.packageName = packageName;
    }
}
