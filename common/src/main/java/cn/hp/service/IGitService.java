package cn.hp.service;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

public interface IGitService {
    /**
     * 克隆一个仓库到本地
     * @param remoteUrl 远程 git 地址
     * @param branch 远程仓库分支
     * @param repoName 本地仓库名
     * @param gitUsername git 用户名
     * @param gitPassword git 密码
     * @return 是否克隆成功
     */
    Boolean cloneRepo(String remoteUrl, String branch, String repoName, String gitUsername, String gitPassword);

    /**
     * 拉取仓库
     * @param repoName 本地仓库名
     * @param branch 远程仓库分支
     * @param gitUsername git 用户名
     * @param gitPassword git 密码
     * @return 是否拉取成功
     */
    Boolean pullRepo(String repoName, String branch, String gitUsername, String gitPassword);

    /**
     * 本地是否存在指定仓库
     * @param repoName 本地仓库名
     * @return 是否存在
     */
    Boolean repoExists(String repoName);

    /**
     * 删除指定的本地仓库
     * @param repoName 仓库名
     * @return 是否删除成功
     */
    Boolean deleteRepo(String repoName);
}
