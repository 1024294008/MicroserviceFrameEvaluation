package cn.hp.resolver;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MicroServiceResolver {
    @Resource
    private ModuleResolver moduleResolver;

}
