package cn.hp.resolver;

import cn.hp.entity.MavenModule;
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
    public List<MavenModule> resolveModule(File project) {
        List<MavenModule> mavenModules = new ArrayList<>();
        scanFile(project, mavenModules);
        return mavenModules;
    }

    private void scanFile(File file, List<MavenModule> mavenModules) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if(subFiles != null){
                Boolean isModule = false;
                MavenModule mavenModule = null;
                for(File subFile: subFiles){
                    if (subFile.isFile() && subFile.getName().equals("pom.xml")) {
                        isModule = true;
                        mavenModule = extractMavenModuleFromPom(new File(file, "pom.xml"));
                        break;
                    }
                }
                if (isModule) {

                    mavenModules.add(mavenModule);
                }
            }
        } else {

        }
    }

    private MavenModule extractMavenModuleFromPom(File pomFile) {
        MavenModule mavenModule = new MavenModule();
        try {
            Document document = Jsoup.parse(pomFile, "utf-8");
            mavenModule.setGroupId(document.select("project > groupId").get(0).text());
            mavenModule.setArtifactId(document.select("project > artifactId").get(0).text());

            PackageType packageType = PackageType.Jar;
            Elements packageElements = document.select("project > packaging");

            if (packageElements.size() > 0
                    && packageElements.get(0).text().equals("pom"))
                packageType = PackageType.Pom;
            mavenModule.setPackageType(packageType);
            mavenModule.setLocation(pomFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mavenModule;
    }
}
