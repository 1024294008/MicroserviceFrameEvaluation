package cn.hp.visitor;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.springframework.stereotype.Component;

@Component
public class EntryASTVisitor extends VoidVisitorAdapter<Void> {
    private MethodDeclaration entryNode;

    public void visit(MethodDeclaration node, Void arg) {
        NodeList<Modifier> modifiers = node.getModifiers();
        if (node.getNameAsString().equals("main")
                && node.getType().toString().equals("void")
                && 1 == node.getParameters().size()
                && 2 == modifiers.size()
                && modifiers.get(0).toString().trim().equals("public")
                && modifiers.get(1).toString().trim().equals("static")) {
            if (node.getParameters().get(0).getType().toString().equals("String[]")) {
                this.entryNode = node;
            }
        }
    }

    public MethodDeclaration getEntryNode() {
        return entryNode;
    }

    public void reset() {
        this.entryNode = null;
    }
}
