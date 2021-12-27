package cn.hp.resolver;

import cn.hp.entity.FeignClientDescription;
import cn.hp.entity.InterfaceFeature;
import cn.hp.visitor.ControllerASTVisitor;
import cn.hp.visitor.EntryASTVisitor;
import cn.hp.visitor.FeignClientVisitor;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;

@Service
public class ASTResolver {
    @Resource
    private EntryASTVisitor entryASTVisitor;

    @Resource
    private ControllerASTVisitor controllerASTVisitor;

    @Resource
    private FeignClientVisitor feignClientVisitor;

    private MethodDeclaration entryNode;

    private List<InterfaceFeature> interfaceFeatures;

    private FeignClientDescription feignClientDescription;

    public void resolveAST(File file) {
        try {
            CompilationUnit compilationUnit = new JavaParser().parse(file).getResult().get();
            compilationUnit.accept(entryASTVisitor, null);
            compilationUnit.accept(controllerASTVisitor, new HashMap<>());
            compilationUnit.accept(feignClientVisitor, new HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.entryNode = entryASTVisitor.getEntryNode();
        this.interfaceFeatures = controllerASTVisitor.getInterfaceFeatures();
        this.feignClientDescription = feignClientVisitor.getFeignClientDescription();
        entryASTVisitor.reset();
        controllerASTVisitor.reset();
        feignClientVisitor.reset();
    }

    public MethodDeclaration getEntryNode() {
        return entryNode;
    }

    public List<InterfaceFeature> getInterfaceFeatures() {
        return interfaceFeatures;
    }

    public FeignClientDescription getFeignClientDescription() {
        return feignClientDescription;
    }

}
