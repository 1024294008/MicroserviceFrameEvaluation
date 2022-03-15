package cn.hp.dao;

import cn.hp.entity.QualityEvaluationDTO;

public interface IQualityEvaluationDao {
    void save(QualityEvaluationDTO qualityEvaluationDTO);

    QualityEvaluationDTO findByMsId(String msId);
}
