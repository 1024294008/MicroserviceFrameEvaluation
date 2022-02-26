package cn.hp.controller;

import cn.hp.entity.MicroServiceDTO;
import cn.hp.entity.MsfeResponse;
import cn.hp.service.IMicroService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/microservices")
public class MicroServiceController {
    @Resource
    private IMicroService microService;

    @GetMapping("/{task_id}")
    public MsfeResponse<List<MicroServiceDTO>> list(@PathVariable("task_id") String taskId) {
        return new MsfeResponse<>(microService.findByTaskId(taskId));
    }
}
