package cn.hp.dao;

import cn.hp.entity.CallGraph;
import org.bson.Document;

public interface ICallRelationDao {
    void save(String taskId, CallGraph callGraph);

    Document findByTaskId(String taskId);
}
