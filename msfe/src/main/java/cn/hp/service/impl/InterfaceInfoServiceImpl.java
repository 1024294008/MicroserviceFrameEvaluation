package cn.hp.service.impl;

import cn.hp.dao.IInterfaceInfoDao;
import cn.hp.entity.InterfaceInfoDTO;
import cn.hp.service.IInterfaceInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InterfaceInfoServiceImpl implements IInterfaceInfoService {
    @Resource
    private IInterfaceInfoDao interfaceInfoDao;

    @Override
    public void save(InterfaceInfoDTO interfaceInfoDTO) {
        interfaceInfoDao.save(interfaceInfoDTO);
    }

    @Override
    public List<InterfaceInfoDTO> findByMsId(String msId) {
        return interfaceInfoDao.findByMsId(msId);
    }
}
