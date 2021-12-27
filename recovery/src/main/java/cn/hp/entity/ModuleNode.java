package cn.hp.entity;

import lombok.Data;

import java.util.List;

@Data
public class ModuleNode {
    private Module module;
    private List<ModuleNode> children;

    public ModuleNode(Module module) {
        this.module = module;
    }
}
