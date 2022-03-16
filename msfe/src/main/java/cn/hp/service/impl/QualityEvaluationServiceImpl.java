package cn.hp.service.impl;

import cn.hp.dao.IQualityEvaluationDao;
import cn.hp.entity.QualityEvaluationDTO;
import cn.hp.service.IQualityEvaluationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class QualityEvaluationServiceImpl implements IQualityEvaluationService {
    @Resource
    private IQualityEvaluationDao qualityEvaluationDao;

    @Override
    public void save(QualityEvaluationDTO qualityEvaluationDTO) {
        qualityEvaluationDao.save(qualityEvaluationDTO);
    }

    @Override
    public List<QualityEvaluationDTO> findByTaskId(String taskId) {
        return qualityEvaluationDao.findByTaskId(taskId);
    }
}
