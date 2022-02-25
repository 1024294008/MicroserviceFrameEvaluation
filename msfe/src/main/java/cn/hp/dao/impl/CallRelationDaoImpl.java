package cn.hp.dao.impl;

import cn.hp.dao.ICallRelationDao;
import cn.hp.entity.CallGraph;
import cn.hp.entity.CallGraphEdge;
import cn.hp.entity.CallGraphNode;
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
public class CallRelationDaoImpl implements ICallRelationDao {
    @Resource
    private MongoTemplate mongoTemplate;

    private static final String CURRENT_COLLECTION = "call_relation";

    @Override
    public void save(String taskId, CallGraph callGraph) {
        Map<String, Object> microServiceCallGraphResult = new HashMap<>();
        microServiceCallGraphResult.put("task_id", taskId);
        microServiceCallGraphResult.put("call_feature", callGraphToDoc(callGraph));

        mongoTemplate.insert(new Document(microServiceCallGraphResult), CURRENT_COLLECTION);
    }

    @Override
    public Document findByTaskId(String taskId) {
        return mongoTemplate.findOne(new Query(Criteria.where("task_id").is(taskId)), Document.class, CURRENT_COLLECTION);
    }

    private Document callGraphToDoc(CallGraph callGraph) {
        Document doc = new Document();
        List<Document> nodeDocs = new ArrayList<>();
        List<Document> linkDocs = new ArrayList<>();
        List<CallGraphNode> nodes = callGraph.getNodes();
        List<CallGraphEdge> edges = callGraph.getEdges();

        for (CallGraphNode node: nodes) {
            nodeDocs.add(callGraphNodeToDoc(node));
        }

        for (CallGraphEdge edge: edges) {
            Document document = new Document();
            document.put("source", callGraphNodeToDoc(edge.getSource()));
            document.put("target", callGraphNodeToDoc(edge.getTarget()));
            linkDocs.add(document);
        }

        doc.put("nodes", nodeDocs);
        doc.put("links", linkDocs);
        return doc;
    }

    private Document callGraphNodeToDoc(CallGraphNode callGraphNode) {
        Document document = new Document();
        document.put("belongService", callGraphNode.getBelongService());
        document.put("apiName", callGraphNode.getApiName());
        return document;
    }
}
