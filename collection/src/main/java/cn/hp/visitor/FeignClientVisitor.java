package cn.hp.visitor;

import cn.hp.entity.FeignClientDescription;
import cn.hp.entity.FeignClientInterfaceDescription;
import cn.hp.util.StrUtil;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.hp.util.AnnotationExprUtil.resolveAnnotationProperties;


@Component
public class FeignClientVisitor extends VoidVisitorAdapter<Map<String, String>> {
    private FeignClientDescription feignClientDescription;

    @Override
    public void visit(ClassOrInterfaceDeclaration node, Map<String, String> arg) {
        arg.put("classType", "0");
        List<AnnotationExpr> annotations = node.getAnnotations();
        if (null == annotations) return;
        for (AnnotationExpr annotation: annotations) {
            if (annotation.getNameAsString().equals("FeignClient")) {
                feignClientDescription = new FeignClientDescription();
                feignClientDescription.setInterfaceName(node.getNameAsString());
                Map<String, String> feignClientProperties = resolveAnnotationProperties(annotation);
                if (feignClientProperties.containsKey("value")) {
                    feignClientDescription.setTargetServiceName(feignClientProperties.get("value"));
                } else if (feignClientProperties.containsKey("name")) {
                    feignClientDescription.setTargetServiceName(feignClientProperties.get("name"));
                }
                arg.put("classType", "1");
                arg.put("className", node.getNameAsString());
            }
            if (annotation.getNameAsString().equals("RequestMapping")) {
                Map<String, String> resolveAnnotationProperties = resolveAnnotationProperties(annotation);
                if (resolveAnnotationProperties.containsKey("value")) arg.put("prefixPath", resolveAnnotationProperties.get("value"));
            }
        }
        super.visit(node, arg);
    }

    @Override
    public void visit(MethodDeclaration node, Map<String, String> arg) {
        if (!arg.containsKey("classType") || !arg.get("classType").equals("1")) return;

        FeignClientInterfaceDescription feignClientInterfaceDescription = new FeignClientInterfaceDescription();
        feignClientInterfaceDescription.setMethodName(node.getNameAsString());

        String prefixPath = "";
        if (arg.containsKey("prefixPath")) prefixPath = arg.get("prefixPath");

        List<AnnotationExpr> annotations = node.getAnnotations();
        if (null == annotations) return;
        for (AnnotationExpr annotation: annotations) {
            switch (annotation.getNameAsString()) {
                case "GetMapping":
                case "PostMapping":
                case "PutMapping":
                case "DeleteMapping":
                case "RequestMapping":
                    Map<String, String> requestProperties = resolveAnnotationProperties(annotation);

                    feignClientInterfaceDescription.setRequestType(annotation.getNameAsString().substring(0,
                            annotation.getNameAsString().indexOf("Mapping")));
                    if (annotation.getNameAsString().equalsIgnoreCase("RequestMapping")) {
                        if (requestProperties.containsKey("method")) {
                            String method = requestProperties.get("method").trim();
                            if (method.startsWith("RequestMethod.") && method.length() > 14) {
                                feignClientInterfaceDescription.setRequestType(StrUtil.capitalize(method.substring(14).toLowerCase()));
                            }
                        }
                    }

                    if (requestProperties.containsKey("value")) feignClientInterfaceDescription.setRequestPath(prefixPath + requestProperties.get("value"));
                    break;
            }
        }

        if (null != feignClientDescription) {
            List<FeignClientInterfaceDescription> feignClientDescriptionInterfaces = feignClientDescription.getInterfaces();
            if (null == feignClientDescriptionInterfaces) {
                feignClientDescriptionInterfaces = new ArrayList<>();
            }
            feignClientDescriptionInterfaces.add(feignClientInterfaceDescription);
            feignClientDescription.setInterfaces(feignClientDescriptionInterfaces);
        }
    }

    public FeignClientDescription getFeignClientDescription() {
        return feignClientDescription;
    }

    public void reset() {
        feignClientDescription = null;
    }
}
