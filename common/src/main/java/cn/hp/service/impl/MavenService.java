package cn.hp.service.impl;

import cn.hp.bean.MavenSetting;
import cn.hp.entity.*;
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
        List<DependencyTreeLog> dependencyTreeLogs = convertDependencyTreeLog(bufferedReader);
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
        List<DependencyTreeLog> dependencyTreeLogs = convertDependencyTreeLog(bufferedReader);
        try {
            if (null != bufferedReader) bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencyTreeLogs;
    }

    @Override
    public List<DependencyAnalyzeLog> resolveUnusedDependencies(Module module) {
        BufferedReader bufferedReader = executeMavenCommand(module, "dependency:analyze");
        if (null == bufferedReader) return null;
        List<DependencyAnalyzeLog> dependencyAnalyzeLogs = new ArrayList<>();
        Pattern moduleStartPattern = Pattern.compile("^\\[INFO] -+< (.+):(.+) >-+$");
        Pattern moduleEndPattern = Pattern.compile("^\\[INFO] -+$");
        try {
            DependencyAnalyzeLog dependencyAnalyzeLog = null;
            List<String> unusedDependencies = null;
            Boolean moduleScanFlag = false;
            Boolean dependencyScanFlag = false;
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                Matcher moduleStartMatcher = moduleStartPattern.matcher(line);
                if (moduleStartMatcher.find()) {
                    moduleScanFlag = true;
                    dependencyScanFlag = false;
                    if (null != dependencyAnalyzeLog) {
                        dependencyAnalyzeLog.setUnusedDependencies(unusedDependencies);
                        dependencyAnalyzeLogs.add(dependencyAnalyzeLog);
                    }
                    unusedDependencies = new ArrayList<>();
                    dependencyAnalyzeLog = new DependencyAnalyzeLog();
                    dependencyAnalyzeLog.setGroupId(moduleStartMatcher.group(1));
                    dependencyAnalyzeLog.setArtifactId(moduleStartMatcher.group(2));
                } else if (moduleEndPattern.matcher(line).find() && moduleScanFlag) {
                    dependencyAnalyzeLog.setUnusedDependencies(unusedDependencies);
                    dependencyAnalyzeLogs.add(dependencyAnalyzeLog);
                    break;
                }
                if (null != dependencyAnalyzeLog && line.trim().equals("[WARNING] Unused declared dependencies found:")) {
                    dependencyScanFlag = true;
                } else if (null != dependencyAnalyzeLog && dependencyScanFlag) {
                    if (line.trim().startsWith("[WARNING]")) {
                        String[] sections = line.split("\\s+");
                        if (sections.length == 2) {
                            unusedDependencies.add(sections[1]);
                        }
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencyAnalyzeLogs;
    }

    private List<DependencyTreeLog> convertDependencyTreeLog(BufferedReader bufferedReader) {
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
                        convertDependencyTreeNode(dependencyTreeLayers, dependencyTreeNode);
                        dependencyTreeLog.setDependencyTreeNode(dependencyTreeNode);
                    }
                    dependencyTreeLog = new DependencyTreeLog();
                    dependencyTreeLog.setGroupId(moduleStartMatcher.group(1));
                    dependencyTreeLog.setArtifactId(moduleStartMatcher.group(2));
                    dependencyTreeLayers.clear();
                } else if (moduleEndPattern.matcher(line).find() && moduleScanFlag) {
                    dependencyTreeLogs.add(dependencyTreeLog);
                    DependencyTreeNode dependencyTreeNode = new DependencyTreeNode("root");
                    convertDependencyTreeNode(dependencyTreeLayers, dependencyTreeNode);
                    dependencyTreeLog.setDependencyTreeNode(dependencyTreeNode);
                    break;
                }
                if (null != dependencyTreeLog && line.startsWith("[INFO] " + dependencyTreeLog.getGroupId() + ":" + dependencyTreeLog.getArtifactId())) {
                    dependencyScanFlag = true;
                } else if (null != dependencyTreeLog && dependencyScanFlag) {
                    if (line.trim().startsWith("[INFO]") && !line.trim().equals("[INFO]")) {
                        dependencyTreeLayers.offer(new DependencyTreeLayer(
                                StrUtil.computeCharNum(line, '|'),
                                line.substring(line.lastIndexOf(" ")).trim()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependencyTreeLogs;
    }

    private void convertDependencyTreeNode(Queue<DependencyTreeLayer> dependencyTreeLayers, DependencyTreeNode parent) {
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
                    convertDependencyTreeNode(dependencyTreeLayers, subDependencyTreeNodes.get(subDependencyTreeNodes.size() - 1));
                } else flag = false;
            } else flag = false;
        }
        parent.setChildren(subDependencyTreeNodes);
    }
}
