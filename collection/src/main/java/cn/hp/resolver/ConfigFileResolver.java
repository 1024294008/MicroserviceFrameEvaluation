package cn.hp.resolver;

import cn.hp.entity.ServiceFeature;
import cn.hp.util.MergeUtil;
import cn.hp.util.YmlUtil;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Service
public class ConfigFileResolver {
    private String profile;
    public ServiceFeature resolveConfig(File configFile) {
        if (!validateConfigFile(configFile)) return null;
        profile = null;

        ServiceFeature serviceFeature = MergeUtil.mergeServiceFeature(
                resolveYmlAndPropertiesFile(new File(configFile.getParent(), "application.yml")),
                resolveYmlAndPropertiesFile(new File(configFile.getParent(), "application.properties"))
        );
        if (null != profile) {
            serviceFeature = MergeUtil.mergeServiceFeature(
                    serviceFeature,
                    MergeUtil.mergeServiceFeature(
                            resolveYmlAndPropertiesFile(new File(configFile.getParent(), "application-" + profile.trim() + ".yml")),
                            resolveYmlAndPropertiesFile(new File(configFile.getParent(), "application-" + profile.trim() + ".properties"))
                    )
            );
        }

        if (null == serviceFeature) serviceFeature = new ServiceFeature();
        serviceFeature.setLocation(configFile);
        return serviceFeature;
    }

    private Boolean validateConfigFile(File configFile) {
        return configFile.exists() && configFile.isFile()
                && (configFile.getName().equals("application.yml") || configFile.getName().equals("application.properties"));
    }

    private ServiceFeature resolveYmlAndPropertiesFile(File configFile) {
        if (!configFile.exists()) return null;
        if (configFile.getName().endsWith("yml")) return resolveYmlConfig(configFile);
        else if (configFile.getName().endsWith("properties")) return resolvePropertiesConfig(configFile);
        return null;
    }

    private ServiceFeature resolveYmlConfig(File configFile) {
        ServiceFeature serviceFeature = new ServiceFeature();
        InputStream is = null;
        try {
            is = new FileInputStream(configFile);
            Map yml = new Yaml().loadAs(is, Map.class);
            serviceFeature.setName(YmlUtil.getProperty(yml, "spring.application.name"));
            serviceFeature.setPort(YmlUtil.getProperty(yml, "server.port"));
            serviceFeature.setContext(YmlUtil.getProperty(yml, "server.context-path"));
            serviceFeature.setRegistryUrl(YmlUtil.getProperty(yml, "eureka.client.service-url.defaultZone"));
            if (configFile.getName().equals("application.yml")) {
                profile = YmlUtil.getProperty(yml, "spring.profiles.active");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null)
                    is.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return serviceFeature;
    }

    private ServiceFeature resolvePropertiesConfig(File configFile) {
        ServiceFeature serviceFeature = new ServiceFeature();
        InputStream is = null;
        try {
            is = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(is);
            serviceFeature.setName(properties.getProperty("spring.application.name"));
            serviceFeature.setPort(properties.getProperty("server.port"));
            serviceFeature.setContext(properties.getProperty("server.context-path"));
            serviceFeature.setRegistryUrl(properties.getProperty("eureka.client.service-url.defaultZone"));
            if (configFile.getName().equals("application.properties")) {
                String tempProfile = properties.getProperty("spring.profiles.active");
                if (null != tempProfile && !tempProfile.trim().equals("")) profile = tempProfile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null)
                    is.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return serviceFeature;
    }
}
