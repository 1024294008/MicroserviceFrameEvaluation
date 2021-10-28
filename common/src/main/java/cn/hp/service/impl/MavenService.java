package cn.hp.service.impl;

import cn.hp.bean.MavenSetting;
import cn.hp.entity.DependencyTreeLayer;
import cn.hp.entity.DependencyLog;
import cn.hp.entity.DependencyTreeNode;
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

    @Override
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

    @Override
    public List<DependencyLog> resolveDependencyTreeIncludes(Module module, String packageName) {
        BufferedReader bufferedReader = executeMavenCommand(module, "dependency:tree -Dverbose -Dincludes=" + packageName);
        List<DependencyLog> dependencyLogs = convertDependencyLog(bufferedReader);
        try {
            if (null != bufferedReader) bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencyLogs;
    }

    @Override
    public List<String> resolveUnusedDependencies(Module module) {
        BufferedReader bufferedReader = executeMavenCommand(module, "dependency:analyze");
        List<String> unusedDependencies = new ArrayList<>();
        if (null == bufferedReader) return unusedDependencies;
        try {
            Boolean scanFlag = false;
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                if (line.trim().equals("[WARNING] Unused declared dependencies found:")) scanFlag = true;
                if (scanFlag && line.trim().startsWith("[WARNING]")) {
                    String[] sections = line.split("\\w+");
                    if (sections.length >= 2) {
                        unusedDependencies.add(sections[1]);
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unusedDependencies;
    }

    private List<DependencyLog> convertDependencyLog(BufferedReader bufferedReader) {
        if (null == bufferedReader) return null;
        List<DependencyLog> dependencyLogs = new ArrayList<>();
        Pattern moduleStartPattern = Pattern.compile("^\\[INFO] -+< (.+):(.+) >-+$");
        Pattern moduleEndPattern = Pattern.compile("^\\[INFO] -+$");
        try {
            DependencyLog dependencyLog = null;
            Queue<DependencyTreeLayer> dependencyTreeLayers = new LinkedList<>();
            Boolean moduleScanFlag = false;
            Boolean dependencyScanFlag = false;
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                Matcher moduleStartMatcher = moduleStartPattern.matcher(line);
                if (moduleStartMatcher.find()) {
                    moduleScanFlag = true;
                    dependencyScanFlag = false;
                    if (null != dependencyLog) {
                        dependencyLogs.add(dependencyLog);
                        DependencyTreeNode dependencyTreeNode = new DependencyTreeNode("root");
                        convertDependencyNode(dependencyTreeLayers, dependencyTreeNode);
                        dependencyLog.setDependencyTreeNode(dependencyTreeNode);
                    }
                    dependencyLog = new DependencyLog();
                    dependencyLog.setGroupId(moduleStartMatcher.group(1));
                    dependencyLog.setArtifactId(moduleStartMatcher.group(2));
                    dependencyTreeLayers.clear();
                } else if (moduleEndPattern.matcher(line).find() && moduleScanFlag) {
                    dependencyLogs.add(dependencyLog);
                    DependencyTreeNode dependencyTreeNode = new DependencyTreeNode();
                    convertDependencyNode(dependencyTreeLayers, dependencyTreeNode);
                    dependencyLog.setDependencyTreeNode(dependencyTreeNode);
                    break;
                }
                if (null != dependencyLog && line.startsWith("[INFO] " + dependencyLog.getGroupId() + ":" + dependencyLog.getArtifactId())) {
                    dependencyScanFlag = true;
                } else if (null != dependencyLog && dependencyScanFlag) {
                    if (!line.trim().equals("[INFO]")) {
                        dependencyTreeLayers.offer(new DependencyTreeLayer(
                                StrUtil.computeCharNum(line, '|'),
                                line.substring(line.lastIndexOf(" "))));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(dependencyLogs);
        return dependencyLogs;
    }

    private void convertDependencyNode(Queue<DependencyTreeLayer> dependencyTreeLayers, DependencyTreeNode parent) {
        if (0 == dependencyTreeLayers.size()) return;
        List<DependencyTreeNode> subDependencyTreeNodes = new ArrayList<>();
        DependencyTreeLayer firstDependencyTreeLayer = dependencyTreeLayers.peek();
        Boolean flag = true;
        while (flag) {
            DependencyTreeLayer currentDependencyTreeLayer = dependencyTreeLayers.peek();
            if (null != currentDependencyTreeLayer) {
                if (currentDependencyTreeLayer.getLayer().equals(firstDependencyTreeLayer.getLayer())) {
                    dependencyTreeLayers.poll();
                    subDependencyTreeNodes.add(new DependencyTreeNode(currentDependencyTreeLayer.getPackageName(), null));
                } else if (currentDependencyTreeLayer.getLayer() > firstDependencyTreeLayer.getLayer()) {
                    convertDependencyNode(dependencyTreeLayers, subDependencyTreeNodes.get(subDependencyTreeNodes.size() - 1));
                } else flag = false;
            } else flag = false;
        }
        parent.setChildren(subDependencyTreeNodes);
    }
}
