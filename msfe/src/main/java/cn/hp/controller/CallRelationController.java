package cn.hp.controller;

import cn.hp.dao.ICallRelationDao;
import cn.hp.entity.MsfeResponse;
import org.bson.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/call_relations")
public class CallRelationController {
    @Resource
    private ICallRelationDao callRelationDao;

    @GetMapping("/{task_id}")
    public MsfeResponse<Document> get(@PathVariable("task_id") String taskId) {
        return new MsfeResponse<>(callRelationDao.findByTaskId(taskId));
    }
}
