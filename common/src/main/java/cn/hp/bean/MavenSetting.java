package cn.hp.bean;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("maven-setting")
public class MavenSetting {

    private String settingFilePath;

    public String getSettingFilePath() {
        return settingFilePath;
    }

    public void setSettingFilePath(String settingFilePath) {
        this.settingFilePath = settingFilePath;
    }
}
