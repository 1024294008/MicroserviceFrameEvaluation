package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeignClientInterfaceDescription {
    private String requestType;
    private String requestPath;
    private String methodName;
}
