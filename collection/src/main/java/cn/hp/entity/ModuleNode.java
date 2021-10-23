package cn.hp.entity;

import lombok.Data;

@Data
public class ModuleNode {
    private Module module;
    private ModuleNode left;
    private ModuleNode right;

    public ModuleNode(Module module) {
        this.module = module;
    }
}
