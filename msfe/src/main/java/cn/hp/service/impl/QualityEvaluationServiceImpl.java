package cn.hp.service.impl;

import cn.hp.dao.IQualityEvaluationDao;
import cn.hp.entity.QualityEvaluationDTO;
import cn.hp.service.IQualityEvaluationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class QualityEvaluationServiceImpl implements IQualityEvaluationService {
    @Resource
    private IQualityEvaluationDao qualityEvaluationDao;

    @Override
    public void save(QualityEvaluationDTO qualityEvaluationDTO) {
        qualityEvaluationDao.save(qualityEvaluationDTO);
    }

    @Override
    public QualityEvaluationDTO findByMsId(String msId) {
        return qualityEvaluationDao.findByMsId(msId);
    }
}
