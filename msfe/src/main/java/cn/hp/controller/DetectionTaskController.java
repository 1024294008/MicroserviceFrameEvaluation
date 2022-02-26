package cn.hp.controller;

import cn.hp.entity.DetectionTaskDTO;
import cn.hp.entity.MsfePageResponse;
import cn.hp.service.IDetectionTaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/detection_tasks")
public class DetectionTaskController {
    @Resource
    private IDetectionTaskService detectionTaskService;

    @GetMapping("")
    public MsfePageResponse<List<DetectionTaskDTO>> list(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageLimit") Integer pageLimit
    ) {
        return new MsfePageResponse<>(detectionTaskService.list(pageNum, pageLimit), pageNum, pageLimit, detectionTaskService.total());
    }
}
