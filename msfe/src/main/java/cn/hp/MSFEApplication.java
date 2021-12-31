package cn.hp;

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

    @Override
    public void run(String... args) {
        testServiceImp.test();
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MSFEApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
