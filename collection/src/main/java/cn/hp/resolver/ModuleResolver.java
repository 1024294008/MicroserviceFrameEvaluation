package cn.hp.resolver;

import cn.hp.entity.Module;
import cn.hp.entity.PackageType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ModuleResolver {
    public List<Module> resolveModule(File project) {
        List<Module> modules = new ArrayList<>();
        scanFile(project, modules);
        return modules;
    }

    private void scanFile(File file, List<Module> modules) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null){
                Boolean isModule = false;
                Module module = null;
                for (File subFile: subFiles){
                    if (subFile.isFile() && subFile.getName().equals("pom.xml")) {
                        isModule = true;
                        module = extractMavenModuleFromPom(new File(file, "pom.xml"));
                        break;
                    }
                }
                if (isModule) {
                    if (module.getPackageType() == PackageType.Pom) {
                        for (File subFile: subFiles) {
                            scanFile(subFile, modules);
                        }
                    }
                    modules.add(module);
                }
            }
        }
    }

    private Module extractMavenModuleFromPom(File pomFile) {
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

            module.setLocation(pomFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return module;
    }
}
