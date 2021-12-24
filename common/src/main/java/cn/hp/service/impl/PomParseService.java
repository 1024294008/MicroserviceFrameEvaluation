package cn.hp.service.impl;

import cn.hp.entity.Module;
import cn.hp.entity.PackageType;
import cn.hp.service.IPomParseService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class PomParseService implements IPomParseService {
    @Override
    public Module parseModule(File pomFile) {
        Module module = new Module();
        try {
            Document document = Jsoup.parse(pomFile, "utf-8");

            Elements groupIdElements = document.select("project > groupId");
            if (groupIdElements.size() > 0) {
                module.setGroupId(groupIdElements.get(0).text());
            } else {
                groupIdElements = document.select("project > parent > groupId");
                if (groupIdElements.size() > 0) {
                    module.setGroupId(groupIdElements.get(0).text());
                }
            }

            module.setArtifactId(document.select("project > artifactId").get(0).text());

            PackageType packageType = PackageType.Jar;
            Elements packageElements = document.select("project > packaging");
            if (packageElements.size() > 0
                    && packageElements.get(0).text().equals("pom"))
                packageType = PackageType.Pom;
            module.setPackageType(packageType);

            module.setLocation(pomFile.getParentFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return module;
    }

    @Override
    public List<String> parseDependencyList(File pomFile) {
        List<String> dependencyList = new ArrayList<>();
        try {
            Document document = Jsoup.parse(pomFile, "utf-8");

            Elements dependencyElements = document.select("dependencies > dependency");
            for (Element dependencyElement: dependencyElements) {
                Elements groupIdElements = dependencyElement.select("groupId");
                Elements artifactIdElements = dependencyElement.select("artifactId");
                Elements versionElements = dependencyElement.select("version");

                String groupId = groupIdElements.size() > 0 ? groupIdElements.get(0).text() : null;
                String artifactId = artifactIdElements.size() > 0 ? artifactIdElements.get(0).text() : null;
                String version = versionElements.size() > 0 ? versionElements.get(0).text() : null;

                if (null != groupId && null != artifactId) {
                    String packageName = groupId + ":" + artifactId;
                    if (null != version) packageName += ":" + version;
                    dependencyList.add(packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencyList;
    }
}
