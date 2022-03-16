package cn.hp.dao;

import cn.hp.entity.QualityEvaluationDTO;

import java.util.List;

public interface IQualityEvaluationDao {
    void save(QualityEvaluationDTO qualityEvaluationDTO);

    List<QualityEvaluationDTO> findByTaskId(String taskId);
}
