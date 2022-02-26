package cn.hp.dao;

import cn.hp.entity.DetectionTaskDTO;

import java.util.List;

public interface IDetectionTaskDao {
    void save(DetectionTaskDTO detectionTaskDTO);

    DetectionTaskDTO findById(String id);

    List<DetectionTaskDTO> list(Integer pageNum, Integer pageLimit);

    Integer total();
}
