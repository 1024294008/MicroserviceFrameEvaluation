package cn.hp.resolver;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

@Service
public class ASTResolver {
    @Resource
    private EntryASTVisitor entryASTVisitor;

    public MethodDeclaration resolveServerEntry(File file) {
        byte[] code = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            code = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(code);
            bufferedInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        astParser.setSource(new String(code).toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
        result.accept(entryASTVisitor);

        MethodDeclaration entryNode = entryASTVisitor.getEntryNode();
        entryASTVisitor.reset();
        return entryNode;
    }
}
