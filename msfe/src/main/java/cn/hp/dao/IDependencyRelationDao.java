package cn.hp.dao;

import cn.hp.entity.DependencyFeature;
import cn.hp.entity.DependencyGraph;
import org.bson.Document;

public interface IDependencyRelationDao {
    void save(String taskId, DependencyFeature dependencyFeature);

    void save(String taskId, DependencyGraph dependencyGraph);

    Document findByTaskId(String taskId);
}
