package cn.hp;

import cn.hp.resolver.MicroServiceResolver;
import cn.hp.service.ITestService;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class MSFEApplication implements CommandLineRunner {
    @Resource
    private ITestService testServiceImp;

    @Resource
    private MicroServiceResolver serviceDivider;

    @Override
    public void run(String... args) {
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MSFEApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
