package cn.hp.resolver;

import cn.hp.entity.Module;
import cn.hp.entity.PackageType;
import cn.hp.service.IPomParseService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleResolver {
    @Resource
    private IPomParseService pomParseService;

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
                        module = pomParseService.parseModule(new File(file, "pom.xml"));
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
}
