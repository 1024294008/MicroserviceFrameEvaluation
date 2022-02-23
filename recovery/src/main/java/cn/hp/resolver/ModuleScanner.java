package cn.hp.resolver;

import cn.hp.entity.*;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleScanner {
    private static final Integer PACKAGE_TYPE_CODE = 0;
    private static final Integer PACKAGE_TYPE_RESOURCE = 1;

    @Resource
    private MicroFrameSetting microFrameSetting;

    @Resource
    private ConfigFileResolver configFileResolver;

    @Resource
    private ASTResolver astResolver;

    @Resource
    private CompResolver compResolver;

    public ModuleFeature scanModule(Module module) {
        ModuleFeature moduleFeature = new ModuleFeature();
        moduleFeature.setModule(module);
        scanFile(new File(module.getLocation(), microFrameSetting.getJavaPath()), moduleFeature, PACKAGE_TYPE_CODE);

        File resourceFile = new File(module.getLocation(), microFrameSetting.getResourcePath());
        if (new File(resourceFile, "application.yml").exists()) {
            scanFile(new File(resourceFile, "application.yml"), moduleFeature, PACKAGE_TYPE_RESOURCE);
        } else if (new File(resourceFile, "application.properties").exists())
            scanFile(new File(resourceFile, "application.properties"), moduleFeature, PACKAGE_TYPE_RESOURCE);

        resolveComp(module, moduleFeature);
        return moduleFeature;
    }

    private void scanFile(File file, ModuleFeature moduleFeature, Integer packageType) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if(subFiles != null){
                for(File subFile: subFiles){
                    scanFile(subFile, moduleFeature, packageType);
                }
            }
        } else {
            if (packageType.equals(PACKAGE_TYPE_CODE)) resolveCode(file, moduleFeature);
            else if (packageType.equals(PACKAGE_TYPE_RESOURCE)) resolveResource(file, moduleFeature);
        }
    }

    private void resolveCode(File file, ModuleFeature moduleFeature) {
        if (!file.getName().endsWith(".java")) return;

        astResolver.resolveAST(file);

        MethodDeclaration entryNode = astResolver.getEntryNode();
        if (null != entryNode) {
            moduleFeature.setCodeFeature(new CodeFeature(file, entryNode.toString()));
        }

        List<InterfaceFeature> interfaceFeatures = astResolver.getInterfaceFeatures();
        if (null != interfaceFeatures) {
            List<InterfaceFeature> moduleInterfaceFeatures = moduleFeature.getInterfaceFeatures();
            if (null == moduleInterfaceFeatures) {
                moduleInterfaceFeatures = new ArrayList<>();
                moduleFeature.setInterfaceFeatures(moduleInterfaceFeatures);
            }
            moduleInterfaceFeatures.addAll(interfaceFeatures);
        }

        FeignClientDescription feignClientDescription = astResolver.getFeignClientDescription();
        List<CallFeature> callFeatures = moduleFeature.getCallFeatures();
        if (null == callFeatures) {
            callFeatures = new ArrayList<>();
            moduleFeature.setCallFeatures(callFeatures);
        }
        if (null != feignClientDescription) {
            List<FeignClientInterfaceDescription> feignClientInterfaceDescriptions = feignClientDescription.getInterfaces();

            for (FeignClientInterfaceDescription feignClientInterfaceDescription: feignClientInterfaceDescriptions) {
                CallFeature callFeature = new CallFeature();
                callFeature.setService(feignClientDescription.getTargetServiceName().toUpperCase());
                callFeature.setApi(feignClientInterfaceDescription.getRequestType()
                        + " " + feignClientInterfaceDescription.getRequestPath());
                callFeatures.add(callFeature);
            }
        }
    }

    private void resolveResource(File file, ModuleFeature moduleFeature) {
        ServiceFeature serviceFeature = configFileResolver.resolveConfig(file);
        if (null != serviceFeature && null == serviceFeature.getName()) serviceFeature.setName(moduleFeature.getModule().getLocation().getName().toUpperCase());
        moduleFeature.setServiceFeature(serviceFeature);
    }

    private void resolveComp(Module module, ModuleFeature moduleFeature) {
        moduleFeature.setDependencyFeature(compResolver.resolveComp(module));
    }
}
