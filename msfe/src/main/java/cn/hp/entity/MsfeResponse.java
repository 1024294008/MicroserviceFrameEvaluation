package cn.hp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsfeResponse<T> {
    private static final String CODE_SUCCESS = "200";
    private static final String CODE_FAIL = "400";
    private static final String MSG_SUCCESS="success";
    private static final String MSG_FAIL="failed";

    private String code = CODE_SUCCESS;
    private String msg = MSG_SUCCESS;
    private T data;

    public MsfeResponse(T data) {
        this.data = data;
    }
}
