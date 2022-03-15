package cn.hp.service;

import cn.hp.entity.QualityEvaluationDTO;

public interface IQualityEvaluationService {
    void save(QualityEvaluationDTO qualityEvaluationDTO);

    QualityEvaluationDTO findByMsId(String msId);
}
