package cn.hp.service;

import cn.hp.entity.QualityEvaluationDTO;

import java.util.List;

public interface IQualityEvaluationService {
    void save(QualityEvaluationDTO qualityEvaluationDTO);

    List<QualityEvaluationDTO> findByTaskId(String taskId);
}
