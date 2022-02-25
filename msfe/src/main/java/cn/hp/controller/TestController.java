package cn.hp.controller;

import cn.hp.dao.IDetectionTaskDao;
import cn.hp.dao.IInterfaceInfoDao;
import cn.hp.dao.IMicroServiceDao;
import cn.hp.entity.DetectionTaskDTO;
import cn.hp.entity.MicroServiceDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("")
public class TestController {
    @Resource
    private IDetectionTaskDao detectionTaskDao;

    @Resource
    private IMicroServiceDao microServiceDao;

    @Resource
    private IInterfaceInfoDao interfaceInfoDao;

    @GetMapping("/test")
    public void test() {
//        detectionTaskDao.save(new DetectionTaskDTO(
//                "1234",
//                "task1",
//                1,
//                0,
//                new Date(),
//                new Date(),
//                "log",
//                "http://xx.git",
//                "master",
//                "hp",
//                "123"
//        ));
        microServiceDao.save(new MicroServiceDTO(
                "22222",
                "1234",
                "msfe",
                "MSFEApplication",
                "msfe",
                "8080",
                "/msfe",
                "http://msfe"
        ));
    }
    @GetMapping("test1")
    public DetectionTaskDTO get() {
        return detectionTaskDao.findById("1234");
    }

    @GetMapping("test2")
    public List<MicroServiceDTO> get2() {
        return microServiceDao.findByTaskId("1234");
    }
}
