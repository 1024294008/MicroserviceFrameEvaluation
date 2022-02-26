package cn.hp.service;

import cn.hp.entity.DetectionTaskDTO;

import java.util.List;

public interface IDetectionTaskService {
    List<DetectionTaskDTO> list(Integer pageNum, Integer pageLimit);

    Integer total();
}
