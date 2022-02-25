package cn.hp.dao.impl;

import cn.hp.dao.IDetectionTaskDao;
import cn.hp.entity.DetectionTaskDTO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class DetectionTaskDaoImpl implements IDetectionTaskDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(DetectionTaskDTO detectionTaskDTO) {
        String sql = "insert into detection_task(id,name,type,status,start_time,end_time,log,git_url,git_branch,git_username,git_password) values(?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(
                sql,
                detectionTaskDTO.getId(),
                detectionTaskDTO.getName(),
                detectionTaskDTO.getType(),
                detectionTaskDTO.getStatus(),
                detectionTaskDTO.getStartTime(),
                detectionTaskDTO.getEndTime(),
                detectionTaskDTO.getLog(),
                detectionTaskDTO.getGitUrl(),
                detectionTaskDTO.getGitBranch(),
                detectionTaskDTO.getGitUsername(),
                detectionTaskDTO.getGitPassword()
        );
    }

    @Override
    public DetectionTaskDTO findById(String id) {
        String sql = "select * from detection_task where id = ?";
        List<DetectionTaskDTO> detectionTaskDTOS = jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(DetectionTaskDTO.class));
        if (detectionTaskDTOS.size() > 0)
            return detectionTaskDTOS.get(0);
        else return null;
    }
}
