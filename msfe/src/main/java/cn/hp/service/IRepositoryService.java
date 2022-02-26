package cn.hp.service;

import cn.hp.entity.DetectionTaskDTO;

import java.io.File;

public interface IRepositoryService {
    Boolean updateRepo(DetectionTaskDTO detectionTaskDTO);

    File findRepo(String repoName);
}