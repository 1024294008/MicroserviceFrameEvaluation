package cn.hp.util;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationExprUtil {
    public static Map<String, String> resolveAnnotationProperties(AnnotationExpr annotation) {
        Map<String, String> annotationProperties = new HashMap<>();
        List<Node> childrenNodes = annotation.getChildNodes();
        childrenNodes = childrenNodes.subList(1, childrenNodes.size());
        for (Node childNode: childrenNodes) {
            if (childNode.toString().matches("^[a-zA-Z_$][a-zA-Z_0-9$]*\\s*=.*$")) {
                Integer splitIndex = childNode.toString().indexOf("=");
                annotationProperties.put(childNode.toString().substring(0, splitIndex).trim(),
                        StrUtil.pruneString(childNode.toString().substring(splitIndex + 1).trim()));
            } else {
                annotationProperties.put("value", StrUtil.pruneString(childNode.toString().trim()));
            }
        }
        return annotationProperties;
    }
}
