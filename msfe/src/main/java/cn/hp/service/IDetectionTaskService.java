package cn.hp.service;

import cn.hp.entity.DetectionTaskDTO;

import java.util.List;

public interface IDetectionTaskService {
    void startMicroServiceDetect(DetectionTaskDTO detectionTaskDTO);

    List<DetectionTaskDTO> list(Integer pageNum, Integer pageLimit);

    Integer total();

    void save(DetectionTaskDTO detectionTaskDTO);

    void update(DetectionTaskDTO detectionTaskDTO);
}
