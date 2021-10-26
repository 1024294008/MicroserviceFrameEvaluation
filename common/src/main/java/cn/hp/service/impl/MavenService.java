package cn.hp.service.impl;

import cn.hp.bean.MavenSetting;
import cn.hp.entity.DependencyLayer;
import cn.hp.entity.DependencyLog;
import cn.hp.entity.DependencyNode;
import cn.hp.entity.Module;
import cn.hp.service.IMavenService;
import cn.hp.util.StrUtil;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.MavenCommandLineBuilder;
import org.apache.maven.shared.utils.cli.Commandline;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        List<DependencyLog> dependencyLogs = convertDependencyLog(bufferedReader);
        try {
            if (null != bufferedReader) bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencyLogs;
    }

    private List<DependencyLog> convertDependencyLog(BufferedReader bufferedReader) {
        if (null == bufferedReader) return null;
        List<DependencyLog> dependencyLogs = new ArrayList<>();
        Pattern moduleStartPattern = Pattern.compile("^\\[INFO] -+< (.+):(.+) >-+$");
        Pattern moduleEndPattern = Pattern.compile("^\\[INFO] -+$");
        try {
            DependencyLog dependencyLog = null;
            Queue<DependencyLayer> dependencyLayers = new LinkedList<>();
            Boolean moduleScanFlag = false;
            Boolean dependencyScanFlag = false;
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                Matcher moduleStartMatcher = moduleStartPattern.matcher(line);
                if (moduleStartMatcher.find()) {
                    moduleScanFlag = true;
                    dependencyScanFlag = false;
                    if (null != dependencyLog) dependencyLogs.add(dependencyLog);
                    dependencyLog = new DependencyLog();
                    dependencyLog.setGroupId(moduleStartMatcher.group(1));
                    dependencyLog.setArtifactId(moduleStartMatcher.group(2));
                    dependencyLog.setDependencyNode(convertDependencyNode(dependencyLayers));
                    dependencyLayers.clear();
                } else if (moduleEndPattern.matcher(line).find() && moduleScanFlag) {
                    dependencyLogs.add(dependencyLog);
                    break;
                }
                if (null != dependencyLog && line.startsWith("[INFO] " + dependencyLog.getGroupId() + ":" + dependencyLog.getArtifactId())) {
                    dependencyScanFlag = true;
                } else if (null != dependencyLog && dependencyScanFlag) {
                    if (!line.trim().equals("[INFO]")) {
                        dependencyLayers.offer(new DependencyLayer(
                                StrUtil.computeCharNum(line, '|'),
                                line.substring(line.lastIndexOf(" "))));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencyLogs;
    }

    private DependencyNode convertDependencyNode(Queue<DependencyLayer> dependencyLayers) {

        return null;
    }
}
