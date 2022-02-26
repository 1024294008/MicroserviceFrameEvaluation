package cn.hp.service.impl;

import cn.hp.dao.IMicroServiceDao;
import cn.hp.entity.MicroServiceDTO;
import cn.hp.service.IMicroService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MicroServiceImpl implements IMicroService {
    @Resource
    private IMicroServiceDao microServiceDao;

    @Override
    public void save(MicroServiceDTO microServiceDTO) {
        microServiceDao.save(microServiceDTO);
    }

    @Override
    public List<MicroServiceDTO> findByTaskId(String taskId) {
        return microServiceDao.findByTaskId(taskId);
    }
}
