package cn.hp.service.impl;

import cn.hp.bean.GitSetting;
import cn.hp.entity.DetectionTaskDTO;
import cn.hp.service.IGitService;
import cn.hp.service.IRepositoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
public class RepositoryServiceImpl implements IRepositoryService {
    @Resource
    private IGitService gitService;

    @Resource
    private GitSetting gitSetting;

    @Override
    public Boolean updateRepo(DetectionTaskDTO detectionTaskDTO) {
        if (!detectionTaskDTO.getGitUrl().matches("^(http(s)?:\\/\\/([^\\/]+?\\/){2}|git@[^:]+:[^\\/]+?\\/).*?.git$"))
            return false;
        String repoName = detectionTaskDTO.getId();
        if (!gitService.repoExists(repoName))
            return gitService.cloneRepo(
                    detectionTaskDTO.getGitUrl(),
                    detectionTaskDTO.getGitBranch(),
                    repoName,
                    detectionTaskDTO.getGitUsername(),
                    detectionTaskDTO.getGitPassword()
            );
        else
            return gitService.pullRepo(
                    repoName,
                    detectionTaskDTO.getGitBranch(),
                    detectionTaskDTO.getGitUsername(),
                    detectionTaskDTO.getGitPassword()
            );
    }

    @Override
    public File findRepo(String repoName){
        return new File(gitSetting.getLocalRepPath(), repoName);
    }
}
