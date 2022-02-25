package cn.hp.dao;

import cn.hp.entity.InterfaceInfoDTO;

import java.util.List;

public interface IInterfaceInfoDao {
    void save (InterfaceInfoDTO interfaceInfoDTO);

    List<InterfaceInfoDTO> findByMsId(String msId);
}
