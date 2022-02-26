package cn.hp.controller;

import cn.hp.entity.DetectionTaskDTO;
import cn.hp.entity.MsfePageResponse;
import cn.hp.entity.MsfeResponse;
import cn.hp.service.IDetectionTaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/detection_tasks")
public class DetectionTaskController {
    @Resource
    private IDetectionTaskService detectionTaskService;

    @GetMapping("/")
    public MsfePageResponse<List<DetectionTaskDTO>> list(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageLimit") Integer pageLimit
    ) {
        return new MsfePageResponse<>(detectionTaskService.list(pageNum, pageLimit), pageNum, pageLimit, detectionTaskService.total());
    }

    @PostMapping("/start_detect")
    public MsfeResponse startMicroServiceDetect(@RequestBody DetectionTaskDTO detectionTaskDTO) {
        detectionTaskService.startMicroServiceDetect(detectionTaskDTO);
        return new MsfeResponse();
    }
}
