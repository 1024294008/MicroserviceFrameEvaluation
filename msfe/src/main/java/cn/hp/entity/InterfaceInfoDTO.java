package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceInfoDTO {
    private String id;
    private String msId;
    private String belongClass;
    private String requestType;
    private String requestPath;
    private String requestParam;
    private String returnResult;
}
