package cn.hp.service;

import cn.hp.entity.MicroServiceDTO;

import java.util.List;

public interface IMicroService {
    void save(MicroServiceDTO microServiceDTO);

    List<MicroServiceDTO> findByTaskId(String taskId);
}
