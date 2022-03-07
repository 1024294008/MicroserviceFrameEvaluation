package cn.hp.service.impl;

import cn.hp.MicroFrameDetector;
import cn.hp.dao.ICallRelationDao;
import cn.hp.dao.IDependencyRelationDao;
import cn.hp.dao.IDetectionTaskDao;
import cn.hp.entity.*;
import cn.hp.service.IDetectionTaskService;
import cn.hp.service.IInterfaceInfoService;
import cn.hp.service.IMicroService;
import cn.hp.service.IRepositoryService;
import cn.hp.util.MicroServiceExecuteLog;
import cn.hp.util.UUIDUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class DetectionTaskServiceImpl implements IDetectionTaskService {
    @Resource
    private IDetectionTaskDao detectionTaskDao;

    @Resource
    private IRepositoryService repositoryService;

    @Resource
    private MicroFrameDetector microFrameDetector;

    @Resource
    private IMicroService microService;

    @Resource
    private IInterfaceInfoService interfaceInfoService;

    @Resource
    private IDependencyRelationDao dependencyRelationDao;

    @Resource
    private ICallRelationDao callRelationDao;

    @Override
    public DetectionTaskDTO findById(String id) {
        return detectionTaskDao.findById(id);
    }

    @Override
    public void startMicroServiceDetect(DetectionTaskDTO detectionTaskDTO) {
        MicroServiceExecuteLog.init();
        MicroServiceExecuteLog.info("Start microservice pattern recognition...");

        if (null == detectionTaskDTO.getGitUsername()) detectionTaskDTO.setGitUsername("");
        if (null == detectionTaskDTO.getGitPassword()) detectionTaskDTO.setGitPassword("");

        String taskId = UUIDUtil.getUUID();
        detectionTaskDTO.setId(taskId);
        detectionTaskDTO.setType(FrameType.Unknown.ordinal());
        detectionTaskDTO.setStatus(DetectStatus.Execute.ordinal());
        detectionTaskDTO.setStartTime(new Date());
        save(detectionTaskDTO);

        try {
            MicroServiceExecuteLog.info("Pull or clone the remote repository...");
            Boolean isPulled = repositoryService.updateRepo(detectionTaskDTO);
            if (isPulled) {
                MicroServiceExecuteLog.info("The remote repository was pulled successfully.");
                File repo = repositoryService.findRepo(taskId);
//                File repo = repositoryService.findRepo("0efb9bfb3dd0481da3c5a0f2e6b26151");
                MicroFrameFeature microFrameFeature = microFrameDetector.getMicroFrameFeature(repo);

                if (0 == microFrameFeature.getModuleFeatures().size()) {
                    detectionTaskDTO.setType(FrameType.Other.ordinal());
                } else {
                    MicroServiceExecuteLog.info("Store dependency features to mongo.");

                    DependencyRelation dependencyRelation = microFrameFeature.getDependencyRelation();
                    if (dependencyRelation.getType().equals(DependencyRelationType.Tree)) {
                        DependencyFeature dependencyFeature = dependencyRelation.getDependencyFeature();
                        dependencyRelationDao.save(taskId, dependencyFeature);
                    } else dependencyRelationDao.save(taskId, dependencyRelation.getDependencyGraph());

                    MicroServiceExecuteLog.info("Store service detail to mysql.");

                    List<ModuleFeature> moduleFeatures = microFrameFeature.getModuleFeatures();
                    for (ModuleFeature moduleFeature: moduleFeatures) {
                        String msId = UUIDUtil.getUUID();
                        microService.save(new MicroServiceDTO(
                                msId,
                                taskId,
                                moduleFeature.getModule().getGroupId() + ":" + moduleFeature.getModule().getArtifactId(),
                                moduleFeature.getCodeFeature().getEntryFile().getName(),
                                moduleFeature.getServiceFeature().getName(),
                                moduleFeature.getServiceFeature().getPort(),
                                moduleFeature.getServiceFeature().getContext(),
                                moduleFeature.getServiceFeature().getRegistryUrl()
                        ));

                        List<InterfaceFeature> interfaceFeatures = moduleFeature.getInterfaceFeatures();
                        for (InterfaceFeature interfaceFeature: interfaceFeatures) {
                            interfaceInfoService.save(new InterfaceInfoDTO(
                                    UUIDUtil.getUUID(),
                                    msId,
                                    interfaceFeature.getBelongClass(),
                                    interfaceFeature.getRequestType(),
                                    interfaceFeature.getRequestPath(),
                                    interfaceFeature.getRequestParam(),
                                    interfaceFeature.getReturnResult()
                            ));
                        }
                    }

                    //将call feature存入mongodb
                    MicroServiceExecuteLog.info("Store call features to mongo.");

                    callRelationDao.save(taskId, microFrameFeature.getCallGraph());
                    detectionTaskDTO.setType(FrameType.Microservice.ordinal());
                }
                MicroServiceExecuteLog.info("End microservice pattern recognition.");
                detectionTaskDTO.setStatus(DetectStatus.Success.ordinal());
            } else {
                MicroServiceExecuteLog.error("The remote repository pull failed.");
                detectionTaskDTO.setStatus(DetectStatus.Fail.ordinal());
            }
        } catch (Exception e) {
            e.printStackTrace();
            MicroServiceExecuteLog.error("The microservice features extract failed.");
            detectionTaskDTO.setStatus(DetectStatus.Fail.ordinal());
        } finally {
            detectionTaskDTO.setEndTime(new Date());
            detectionTaskDTO.setLog(MicroServiceExecuteLog.getLog());
            update(detectionTaskDTO);
        }
    }

    @Override
    public List<DetectionTaskDTO> list(Integer pageNum, Integer pageLimit) {
        return detectionTaskDao.list(pageNum, pageLimit);
    }

    @Override
    public Integer total() {
        return detectionTaskDao.total();
    }

    @Override
    public void save(DetectionTaskDTO detectionTaskDTO) {
        detectionTaskDao.save(detectionTaskDTO);
    }

    @Override
    public void update(DetectionTaskDTO detectionTaskDTO) {
        detectionTaskDao.update(detectionTaskDTO);
    }
}
