package cn.hp.controller;

import cn.hp.entity.MsfeResponse;
import cn.hp.entity.QualityEvaluationDTO;
import cn.hp.service.IQualityEvaluationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/quality_evaluations")
public class QualityEvaluationController {
    @Resource
    private IQualityEvaluationService qualityEvaluationService;

    @GetMapping("/{task_id}")
    public MsfeResponse<List<QualityEvaluationDTO>> list(@PathVariable("task_id") String taskId) {
        return new MsfeResponse<>(qualityEvaluationService.findByTaskId(taskId));
    }
}
