package cn.hp.service.impl;

import cn.hp.service.ITestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements ITestService {
    @Override
    public void test() {
        System.out.println("test");
    }
}
