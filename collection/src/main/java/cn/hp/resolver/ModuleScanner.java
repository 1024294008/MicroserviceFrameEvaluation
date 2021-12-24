package cn.hp.resolver;

import cn.hp.bean.ProjectInfo;
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

    @Resource
    private ProjectInfo projectInfo;

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
            if (packageType.equals(PACKAGE_TYPE_CODE)) {
                resolveCode(file, moduleFeature);
                resolveFeignClient(file, moduleFeature);
            }
            else if (packageType.equals(PACKAGE_TYPE_RESOURCE)) resolveResource(file, moduleFeature);
        }
    }

    private void resolveCode(File file, ModuleFeature moduleFeature) {
        if (file.getName().endsWith(".java")) {
            astResolver.resolveAST(file);

            MethodDeclaration entryNode = astResolver.getEntryNode();
            if (null != entryNode) {
                moduleFeature.setCodeFeature(new CodeFeature(file, entryNode.toString()));
            }

            List<InterfaceFeature> interfaceFeatures = astResolver.getInterfaceFeatures();
            if (null != interfaceFeatures && 0 < interfaceFeatures.size()) {
                List<InterfaceFeature> moduleInterfaceFeatures = moduleFeature.getInterfaceFeatures();
                if (null == moduleInterfaceFeatures) {
                    moduleInterfaceFeatures = new ArrayList<>();
                    moduleFeature.setInterfaceFeatures(moduleInterfaceFeatures);
                }
                moduleInterfaceFeatures.addAll(interfaceFeatures);
            }
        }
    }

    private void resolveFeignClient(File file, ModuleFeature moduleFeature) {
        FeignClientDescription feignClientDescription = astResolver.getFeignClientDescription();
        List<CallFeature> callFeatures = moduleFeature.getCallFeatures();
        if (null == callFeatures) {
            callFeatures = new ArrayList<>();
            moduleFeature.setCallFeatures(callFeatures);
        }

        if (null != feignClientDescription) {
            List<FeignClientInterfaceDescription> feignClientInterfaceDescriptions = feignClientDescription.getInterfaces();

            for (FeignClientInterfaceDescription feignClientInterfaceDescription: feignClientInterfaceDescriptions) {
                List<String> callerInterfaces = new ArrayList<>();
                CallFeature callFeature = new CallFeature();
                callFeature.setBelongService(feignClientDescription.getTargetServiceName());
                callFeature.setApiName(feignClientInterfaceDescription.getRequestType()
                    + " " + feignClientInterfaceDescription.getRequestPath());


            }
        }
    }

    private void resolveResource(File file, ModuleFeature moduleFeature) {
        moduleFeature.setServiceFeature(configFileResolver.resolveConfig(file));
    }

    private void resolveComp(Module module, ModuleFeature moduleFeature) {
        moduleFeature.setDependencyFeature(compResolver.resolveComp(module));
    }
}
