package cn.hp.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("git-setting")
public class GitSetting {
    private String localRepPath;

    public String getLocalRepPath() {
        return localRepPath;
    }

    public void setLocalRepPath(String localRepPath) {
        this.localRepPath = localRepPath;
    }
}
