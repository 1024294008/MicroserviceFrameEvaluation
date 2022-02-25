package cn.hp.dao;

import cn.hp.entity.DetectionTaskDTO;

public interface IDetectionTaskDao {
    void save(DetectionTaskDTO detectionTaskDTO);

    DetectionTaskDTO findById(String id);
}
