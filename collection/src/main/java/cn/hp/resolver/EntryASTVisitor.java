package cn.hp.resolver;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import org.springframework.stereotype.Component;

@Component
public class EntryASTVisitor extends VoidVisitorAdapter<Void> {
    private MethodDeclaration entryNode;

    @Override
    public void visit(MethodDeclaration node, Void arg) {
        if (node.getName().equals("main")
                && node.getType().toString().equals("void")
                && 1 == node.getParameters().size()
                && 9 == node.getModifiers()) {
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
