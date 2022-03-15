package cn.hp.controller;

import cn.hp.entity.MsfeResponse;
import cn.hp.entity.QualityEvaluationDTO;
import cn.hp.service.IQualityEvaluationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/quality_evaluation")
public class QualityEvaluationController {
    @Resource
    private IQualityEvaluationService qualityEvaluationService;

    @GetMapping("/{ms_id}")
    public MsfeResponse<QualityEvaluationDTO> get(@PathVariable("ms_id") String msId) {
        return new MsfeResponse<>(qualityEvaluationService.findByMsId(msId));
    }
}
