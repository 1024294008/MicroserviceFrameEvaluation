package cn.hp.config;

import cn.hp.entity.MicroFrameSetting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MicroFrameConfig {
    @Bean
    public MicroFrameSetting microFrameSetting() {
        Path commonPath = Paths.get("src", "main");
        return new MicroFrameSetting(
                Paths.get(commonPath.toString(), "java").toString(),
                Paths.get(commonPath.toString(), "resources").toString(),
                Paths.get("pom.xml").toString()
        );
    }
}
