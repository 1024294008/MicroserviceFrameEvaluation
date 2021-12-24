package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MicroFrameSetting {
    private String javaPath;
    private String resourcePath;
    private String pomPath;
}