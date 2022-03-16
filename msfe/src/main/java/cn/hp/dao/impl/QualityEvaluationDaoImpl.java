package cn.hp.dao.impl;

import cn.hp.dao.IQualityEvaluationDao;
import cn.hp.entity.QualityEvaluationDTO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class QualityEvaluationDaoImpl implements IQualityEvaluationDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(QualityEvaluationDTO qualityEvaluationDTO) {
        String sql = "insert into quality_evaluation(id,task_id,service_name,adaptation,security_component,self_invocation,load_balance_component,service_registry_center,cpa) values(?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(
                sql,
                qualityEvaluationDTO.getId(),
                qualityEvaluationDTO.getTaskId(),
                qualityEvaluationDTO.getServiceName(),
                qualityEvaluationDTO.getAdaptation(),
                qualityEvaluationDTO.getSecurityComponent(),
                qualityEvaluationDTO.getSelfInvocation(),
                qualityEvaluationDTO.getLoadBalanceComponent(),
                qualityEvaluationDTO.getServiceRegistryCenter(),
                qualityEvaluationDTO.getCpa()
        );
    }

    @Override
    public List<QualityEvaluationDTO> findByTaskId(String taskId) {
        String sql = "select * from quality_evaluation where task_id = ?";
        return jdbcTemplate.query(sql, new Object[]{taskId}, new BeanPropertyRowMapper<>(QualityEvaluationDTO.class));
    }
}
