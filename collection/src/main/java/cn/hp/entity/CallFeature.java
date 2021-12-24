package cn.hp.entity;

import lombok.Data;

import java.util.List;

@Data
public class CallFeature {
    private String belongService;
    private String apiName;
    private List<String> callers;
}
