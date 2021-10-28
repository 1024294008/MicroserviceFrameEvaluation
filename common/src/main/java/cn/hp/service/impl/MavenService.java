package cn.hp.service.impl;

import cn.hp.bean.MavenSetting;
import cn.hp.entity.DependencyTreeLayer;
import cn.hp.entity.DependencyTreeLog;
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
    public List<DependencyTreeLog> resolveDependencyTree(Module module) {
        BufferedReader bufferedReader = executeMavenCommand(module, "dependency:tree");
        List<DependencyTreeLog> dependencyTreeLogs = convertDependencyLog(bufferedReader);
        try {
            if (null != bufferedReader) bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencyTreeLogs;
    }

    @Override
    public List<DependencyTreeLog> resolveDependencyTreeIncludes(Module module, String packageName) {
        BufferedReader bufferedReader = executeMavenCommand(module, "dependency:tree -Dverbose -Dincludes=" + packageName);
        List<DependencyTreeLog> dependencyTreeLogs = convertDependencyLog(bufferedReader);
        try {
            if (null != bufferedReader) bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencyTreeLogs;
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

    private List<DependencyTreeLog> convertDependencyLog(BufferedReader bufferedReader) {
        if (null == bufferedReader) return null;
        List<DependencyTreeLog> dependencyTreeLogs = new ArrayList<>();
        Pattern moduleStartPattern = Pattern.compile("^\\[INFO] -+< (.+):(.+) >-+$");
        Pattern moduleEndPattern = Pattern.compile("^\\[INFO] -+$");
        try {
            DependencyTreeLog dependencyTreeLog = null;
            Queue<DependencyTreeLayer> dependencyTreeLayers = new LinkedList<>();
            Boolean moduleScanFlag = false;
            Boolean dependencyScanFlag = false;
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                Matcher moduleStartMatcher = moduleStartPattern.matcher(line);
                if (moduleStartMatcher.find()) {
                    moduleScanFlag = true;
                    dependencyScanFlag = false;
                    if (null != dependencyTreeLog) {
                        dependencyTreeLogs.add(dependencyTreeLog);
                        DependencyTreeNode dependencyTreeNode = new DependencyTreeNode("root");
                        convertDependencyNode(dependencyTreeLayers, dependencyTreeNode);
                        dependencyTreeLog.setDependencyTreeNode(dependencyTreeNode);
                    }
                    dependencyTreeLog = new DependencyTreeLog();
                    dependencyTreeLog.setGroupId(moduleStartMatcher.group(1));
                    dependencyTreeLog.setArtifactId(moduleStartMatcher.group(2));
                    dependencyTreeLayers.clear();
                } else if (moduleEndPattern.matcher(line).find() && moduleScanFlag) {
                    dependencyTreeLogs.add(dependencyTreeLog);
                    DependencyTreeNode dependencyTreeNode = new DependencyTreeNode();
                    convertDependencyNode(dependencyTreeLayers, dependencyTreeNode);
                    dependencyTreeLog.setDependencyTreeNode(dependencyTreeNode);
                    break;
                }
                if (null != dependencyTreeLog && line.startsWith("[INFO] " + dependencyTreeLog.getGroupId() + ":" + dependencyTreeLog.getArtifactId())) {
                    dependencyScanFlag = true;
                } else if (null != dependencyTreeLog && dependencyScanFlag) {
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
        System.out.println(dependencyTreeLogs);
        return dependencyTreeLogs;
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
