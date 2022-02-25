package cn.hp.dao.impl;

import cn.hp.dao.IMicroServiceDao;
import cn.hp.entity.MicroServiceDTO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class MicroServiceDaoImpl implements IMicroServiceDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(MicroServiceDTO microServiceDTO) {
        String sql = "insert into microservice(id,task_id,module_name,entry_class,service_name,port,context,registry_url) values(?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(
                sql,
                microServiceDTO.getId(),
                microServiceDTO.getTaskId(),
                microServiceDTO.getModuleName(),
                microServiceDTO.getEntryClass(),
                microServiceDTO.getServiceName(),
                microServiceDTO.getPort(),
                microServiceDTO.getContext(),
                microServiceDTO.getRegistryUrl()
        );
    }

    @Override
    public List<MicroServiceDTO> findByTaskId(String taskId) {
        String sql = "select * from microservice where task_id = ?";
        return jdbcTemplate.query(sql, new Object[]{taskId}, new BeanPropertyRowMapper<>(MicroServiceDTO.class));
    }
}
