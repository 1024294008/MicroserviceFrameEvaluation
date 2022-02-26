package cn.hp.service.impl;

import cn.hp.bean.GitSetting;
import cn.hp.service.IGitService;
import cn.hp.util.FileUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GitService implements IGitService {
    private final GitSetting gitSetting;

    @Autowired
    public GitService(GitSetting gitSetting) {
        this.gitSetting = gitSetting;
    }

    @Override
    public Boolean cloneRepo(
            String remoteUrl,
            String branch,
            String repoName,
            String gitUsername,
            String gitPassword
    ) {
        boolean resultFlag = false;
        Git git = null;
        try {
            git = Git.cloneRepository()
                    .setURI(remoteUrl)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUsername, gitPassword))
                    .setBranch(branch)
                    .setDirectory(new File(this.gitSetting.getLocalRepPath(), repoName)).call();
            resultFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (git != null) git.close();
        }
        return resultFlag;
    }

    @Override
    public Boolean pullRepo(String repoName, String branch, String gitUsername, String gitPassword) {
        boolean resultFlag = false;
        Git git = null;
        try {
            git = new Git(new FileRepository(new File(this.gitSetting.getLocalRepPath(), repoName + File.separator + ".git")));
            git.pull().setRemoteBranchName(branch)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUsername, gitPassword)).call();
            resultFlag = true;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        } finally {
            if (git != null) git.close();
        }
        return resultFlag;
    }

    @Override
    public Boolean repoExists(String repoName) {
        File gitFile = new File(this.gitSetting.getLocalRepPath(), repoName + File.separator + ".git");
        return gitFile.exists() && gitFile.isDirectory();
    }

    @Override
    public Boolean deleteRepo(String repoName) {
        File repoFile = new File(this.gitSetting.getLocalRepPath(), repoName);

        if (repoFile.exists()) {
            return FileUtil.deleteDir(repoFile);
        }
        return true;
    }
}
