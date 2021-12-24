package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeignClientDescription {
    private String interfaceName;
    private String targetServiceName;
    private List<FeignClientInterfaceDescription> interfaces;
}
