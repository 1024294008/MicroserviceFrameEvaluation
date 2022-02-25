package cn.hp.dao;

import cn.hp.entity.MicroServiceDTO;

import java.util.List;

public interface IMicroServiceDao {
    void save(MicroServiceDTO microServiceDTO);

    List<MicroServiceDTO> findByTaskId(String taskId);
}
