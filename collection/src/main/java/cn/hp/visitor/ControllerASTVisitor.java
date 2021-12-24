package cn.hp.visitor;

import cn.hp.entity.InterfaceFeature;
import cn.hp.util.StrUtil;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hp.util.AnnotationExprUtil.resolveAnnotationProperties;

@Component
public class ControllerASTVisitor extends VoidVisitorAdapter<Map<String, String>> {
    private List<InterfaceFeature> interfaceFeatures = new ArrayList<>();

    @Override
    public void visit(ClassOrInterfaceDeclaration node, Map<String, String> arg) {
        arg.put("classType", "0");
        List<AnnotationExpr> annotations = node.getAnnotations();
        for (AnnotationExpr annotation: annotations) {
            if (annotation.getNameAsString().equals("RestController")
                    || annotation.getNameAsString().equals("Controller")) {
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

        InterfaceFeature interfaceFeature = new InterfaceFeature();

        String prefixPath = "";
        if (arg.containsKey("prefixPath")) prefixPath = arg.get("prefixPath");

        List<AnnotationExpr> annotations = node.getAnnotations();
        for (AnnotationExpr annotation: annotations) {
            switch (annotation.getNameAsString()) {
                case "GetMapping":
                case "PostMapping":
                case "PutMapping":
                case "DeleteMapping":
                case "RequestMapping":
                    Map<String, String> requestProperties = resolveAnnotationProperties(annotation);

                    interfaceFeature.setRequestType(annotation.getNameAsString().substring(0,
                            annotation.getNameAsString().indexOf("Mapping")));
                    if (annotation.getNameAsString().equalsIgnoreCase("RequestMapping")) {
                        if (requestProperties.containsKey("method")) {
                            String method = requestProperties.get("method").trim();
                            if (method.startsWith("RequestMethod.") && method.length() > 14) {
                                interfaceFeature.setRequestType(StrUtil.capitalize(method.substring(14).toLowerCase()));
                            }
                        }
                    }

                    interfaceFeature.setBelongClass(arg.get("className"));

                    Map<String, String> paramMap = new HashMap<>();
                    List<Parameter> parameters = node.getParameters();
                    for (Parameter parameter: parameters) {
                        String value = parameter.toString().trim().substring(parameter.toString().trim().lastIndexOf(" ") + 1);

                        List<AnnotationExpr> parameterAnnotations = parameter.getAnnotations();
                        for (AnnotationExpr parameterAnnotation: parameterAnnotations) {
                            if (parameterAnnotation.getNameAsString().equalsIgnoreCase("RequestParam")) {
                                Map<String, String> paramProperties = resolveAnnotationProperties(parameterAnnotation);
                                if (paramProperties.containsKey("name")) value = paramProperties.get("name");
                            }
                        }
                        if (parameterAnnotations.size() > 0)
                            paramMap.put(value, parameter.getType().toString());
                    }
                    interfaceFeature.setRequestParam(mapToJson(paramMap));

                    interfaceFeature.setReturnResult(node.getType().toString());
                    if (requestProperties.containsKey("value")) interfaceFeature.setRequestPath(prefixPath + requestProperties.get("value"));
                    break;
                case "ApiOperation":
                    Map<String, String> swaggerProperties = resolveAnnotationProperties(annotation);
                    if (swaggerProperties.containsKey("value")) interfaceFeature.setServiceName(swaggerProperties.get("value"));
                    break;
            }
        }
        if (null != interfaceFeature.getBelongClass())
            this.interfaceFeatures.add(interfaceFeature);
    }

    private String mapToJson(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        String tap = "\t";
        sb.append("{\n");

        for (String key: map.keySet()) {
            sb.append(tap).append("\"").append(key).append("\"").append(": ").append(map.get(key)).append("\n");
        }

        sb.append("}");
        return sb.toString();
    }

    public List<InterfaceFeature> getInterfaceFeatures() {
        return interfaceFeatures;
    }

    public void reset() {
        this.interfaceFeatures = new ArrayList<>();
    }
}
