package cn.hp.dao.impl;

import cn.hp.dao.IInterfaceInfoDao;
import cn.hp.entity.InterfaceInfoDTO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class InterfaceInfoDaoImpl implements IInterfaceInfoDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(InterfaceInfoDTO interfaceInfoDTO) {
        String sql = "insert into interface_info(id,ms_id,belong_class,request_type,request_path,request_param,return_result) values(?,?,?,?,?,?,?)";
        jdbcTemplate.update(
                sql,
                interfaceInfoDTO.getId(),
                interfaceInfoDTO.getMsId(),
                interfaceInfoDTO.getBelongClass(),
                interfaceInfoDTO.getRequestType(),
                interfaceInfoDTO.getRequestPath(),
                interfaceInfoDTO.getRequestParam(),
                interfaceInfoDTO.getReturnResult()
        );
    }

    @Override
    public List<InterfaceInfoDTO> findByMsId(String msId) {
        String sql = "select * from interface_info where ms_id = ?";
        return jdbcTemplate.query(sql, new Object[]{msId}, new BeanPropertyRowMapper<>(InterfaceInfoDTO.class));
    }
}
