package cn.hp.resolver;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.springframework.stereotype.Component;

@Component
public class EntryASTVisitor extends ASTVisitor {
    private MethodDeclaration entryNode;

    @Override
    public boolean visit(MethodDeclaration node) {
        if (node.getName().toString().equals("main")
                && node.getReturnType2().toString().equals("void")
                && 1 == node.parameters().size()
                && 2 == node.modifiers().size()
                && node.modifiers().get(0).toString().equals("public")
                && node.modifiers().get(1).toString().equals("static")) {
            String[] parameters = node.parameters().get(0).toString().split(" ");
            if (2 == parameters.length && parameters[0].equals("String[]")) {
                this.entryNode = node;
            }
        }
        return true;
    }

    public MethodDeclaration getEntryNode() {
        return entryNode;
    }

    public void reset() {
        this.entryNode = null;
    }
}
