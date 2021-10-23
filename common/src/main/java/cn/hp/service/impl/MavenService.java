package cn.hp.service.impl;

import cn.hp.bean.MavenSetting;
import cn.hp.entity.DependencyLog;
import cn.hp.entity.Module;
import cn.hp.service.IMavenService;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.MavenCommandLineBuilder;
import org.apache.maven.shared.utils.cli.Commandline;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MavenService implements IMavenService {
    @Resource
    private MavenSetting mavenSetting;

    private BufferedReader executeMavenCommand(Module module, String command) {
        Process process = null;
        InvocationRequest request = new DefaultInvocationRequest();
        MavenCommandLineBuilder builder = new MavenCommandLineBuilder();
        String pomPath = new File(module.getLocation(), "pom.xml").getAbsolutePath();

        request.setPomFile(new File(pomPath));
        request.setGoals(Collections.singletonList(command + " -s " + mavenSetting.getSettingFilePath()));

        try {
            Commandline commandline = builder.build(request);
            process = commandline.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == process) return null;
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public List<DependencyLog> resolveDependencyTree(Module module) {
        BufferedReader bufferedReader = executeMavenCommand(module, "dependency:tree");
        if (null == bufferedReader) return null;
        List<DependencyLog> dependencyLogs = new ArrayList<>();
        try {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
