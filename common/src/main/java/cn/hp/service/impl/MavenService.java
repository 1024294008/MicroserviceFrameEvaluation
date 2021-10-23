package cn.hp.service.impl;

import cn.hp.bean.MavenSetting;
import cn.hp.service.IMavenService;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.MavenCommandLineBuilder;
import org.apache.maven.shared.utils.cli.Commandline;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collections;

@Service
public class MavenService implements IMavenService {
    @Resource
    private MavenSetting mavenSetting;

    private Process executeMavenCommand(File workspace, String command) {
        Process process = null;
        InvocationRequest request = new DefaultInvocationRequest();
        MavenCommandLineBuilder builder = new MavenCommandLineBuilder();
        String pomPath = new File(workspace, "pom.xml").getAbsolutePath(); // 测试当前工作目录下的pom文件

        request.setPomFile(new File(pomPath));
        request.setGoals(Collections.singletonList(command + " -s " + mavenSetting.getSettingFilePath()));

        try {
            Commandline commandline = builder.build(request);
            process = commandline.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return process;
    }
}
