package cn.hp.service.impl;

import cn.hp.dao.IDetectionTaskDao;
import cn.hp.entity.DetectionTaskDTO;
import cn.hp.service.IDetectionTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DetectionTaskServiceImpl implements IDetectionTaskService {
    @Resource
    private IDetectionTaskDao detectionTaskDao;

    @Override
    public List<DetectionTaskDTO> list(Integer pageNum, Integer pageLimit) {
        return detectionTaskDao.list(pageNum, pageLimit);
    }

    @Override
    public Integer total() {
        return detectionTaskDao.total();
    }
}
