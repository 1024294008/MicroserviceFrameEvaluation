package cn.hp.service;

import cn.hp.entity.InterfaceInfoDTO;

import java.util.List;

public interface IInterfaceInfoService {
    void save (InterfaceInfoDTO interfaceInfoDTO);

    List<InterfaceInfoDTO> findByMsId(String msId);
}
