package cn.hp.resolver;

import japa.parser.JavaParser;
import japa.parser.TokenMgrError;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
public class ASTResolver {
    @Resource
    private EntryASTVisitor entryASTVisitor;

    public MethodDeclaration resolveServerEntry(File file) {
        try {
            CompilationUnit compilationUnit = JavaParser.parse(file);
            compilationUnit.accept(entryASTVisitor, null);
        } catch (TokenMgrError e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        MethodDeclaration entryNode = entryASTVisitor.getEntryNode();
        entryASTVisitor.reset();
        return entryNode;
    }
}
