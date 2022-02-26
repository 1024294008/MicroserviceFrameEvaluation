package cn.hp.controller;

import cn.hp.entity.InterfaceInfoDTO;
import cn.hp.entity.MsfeResponse;
import cn.hp.service.IInterfaceInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/interface_info")
public class InterfaceInfoController {
    @Resource
    private IInterfaceInfoService interfaceInfoService;

    @GetMapping("/{ms_id}")
    public MsfeResponse<List<InterfaceInfoDTO>> list(@PathVariable("ms_id") String msId) {
        return new MsfeResponse<>(interfaceInfoService.findByMsId(msId));
    }
}
