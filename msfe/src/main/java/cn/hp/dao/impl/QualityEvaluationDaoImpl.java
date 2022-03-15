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
        String sql = "insert into quality_evaluation(id,ms_id,adaptation,security_component,self_invocation,load_balance_component,service_registry_center,cpa) values(?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(
                sql,
                qualityEvaluationDTO.getId(),
                qualityEvaluationDTO.getMsId(),
                qualityEvaluationDTO.getAdaptation(),
                qualityEvaluationDTO.getSecurityComponent(),
                qualityEvaluationDTO.getSelfInvocation(),
                qualityEvaluationDTO.getLoadBalanceComponent(),
                qualityEvaluationDTO.getServiceRegistryCenter(),
                qualityEvaluationDTO.getCpa()
        );
    }

    @Override
    public QualityEvaluationDTO findByMsId(String msId) {
        String sql = "select * from quality_evaluation where task_id = ?";
        List<QualityEvaluationDTO> qualityEvaluationDTOS = jdbcTemplate.query(sql, new Object[]{msId}, new BeanPropertyRowMapper<>(QualityEvaluationDTO.class));
        if (qualityEvaluationDTOS.size() > 0)
            return qualityEvaluationDTOS.get(0);
        return null;
    }
}
