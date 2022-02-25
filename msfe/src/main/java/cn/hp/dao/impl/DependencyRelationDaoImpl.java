package cn.hp.dao.impl;

import cn.hp.dao.IDependencyRelationDao;
import cn.hp.entity.DependencyFeature;
import cn.hp.entity.DependencyGraph;
import cn.hp.entity.DependencyGraphEdge;
import cn.hp.entity.DependencyGraphNode;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DependencyRelationDaoImpl implements IDependencyRelationDao {
    @Resource
    private MongoTemplate mongoTemplate;

    private static final String CURRENT_COLLECTION = "dependency_relation";

    @Override
    public void save(String taskId, DependencyFeature dependencyFeature) {
        Map<String, Object> microServiceDependencyFeatureResult = new HashMap<>();
        microServiceDependencyFeatureResult.put("task_id", taskId);
        microServiceDependencyFeatureResult.put("type", "dependency_tree");
        microServiceDependencyFeatureResult.put("dependency_feature", dependencyFeatureToDoc(dependencyFeature));

        mongoTemplate.insert(new Document(microServiceDependencyFeatureResult), CURRENT_COLLECTION);
    }

    @Override
    public void save(String taskId, DependencyGraph dependencyGraph) {
        Map<String, Object> microServiceDependencyFeatureResult = new HashMap<>();
        microServiceDependencyFeatureResult.put("task_id", taskId);
        microServiceDependencyFeatureResult.put("type", "dependency_graph");
        microServiceDependencyFeatureResult.put("dependency_feature", dependencyGraphToDoc(dependencyGraph));

        mongoTemplate.insert(new Document(microServiceDependencyFeatureResult), CURRENT_COLLECTION);
    }

    @Override
    public Document findByTaskId(String taskId) {
        return mongoTemplate.findOne(new Query(Criteria.where("task_id").is(taskId)), Document.class, CURRENT_COLLECTION);
    }

    private Document dependencyFeatureToDoc(DependencyFeature dependencyFeature) {
        Document doc = new Document();
        List<DependencyFeature> subDependencyFeatures = dependencyFeature.getChildren();
        List<Document> subDocs = new ArrayList<>();

        for (DependencyFeature subDependencyFeature: subDependencyFeatures) {
            subDocs.add(dependencyFeatureToDoc(subDependencyFeature));
        }

        doc.put("name", dependencyFeature.getValue());
        doc.put("value", dependencyFeature.getType().toString());
        doc.put("children", subDocs);
        return doc;
    }

    private Document dependencyGraphToDoc(DependencyGraph dependencyGraph) {
        Document doc = new Document();
        List<Document> nodeDocs = new ArrayList<>();
        List<Document> linkDocs = new ArrayList<>();
        List<DependencyGraphNode> nodes = dependencyGraph.getNodes();
        List<DependencyGraphEdge> edges = dependencyGraph.getEdges();

        for (DependencyGraphNode node: nodes) {
            Document document = new Document();
            document.put("name", node.getValue());
            document.put("type", node.getType().toString());
            nodeDocs.add(document);
        }

        for (DependencyGraphEdge edge: edges) {
            Document document = new Document();
            document.put("source", edge.getSource());
            document.put("target", edge.getTarget());
            document.put("type", edge.getType().toString());
            linkDocs.add(document);
        }

        doc.put("nodes", nodeDocs);
        doc.put("links", linkDocs);
        return doc;
    }
}
