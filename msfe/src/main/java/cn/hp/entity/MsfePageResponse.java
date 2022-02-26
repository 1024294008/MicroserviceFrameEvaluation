package cn.hp.entity;

import lombok.Data;

@Data
public class MsfePageResponse<T> {
    private static final String CODE_SUCCESS = "200";
    private static final String CODE_FAIL = "400";
    private static final String MSG_SUCCESS="success";
    private static final String MSG_FAIL="failed";

    private String code = CODE_SUCCESS;
    private String msg = MSG_SUCCESS;
    private Integer currentPage;
    private Integer pageLimit;
    private Integer total;
    private T data;

    public MsfePageResponse(T data, Integer currentPage, Integer pageLimit, Integer total) {
        this.data = data;
        this.currentPage = currentPage;
        this.pageLimit = pageLimit;
        this.total = total;
    }
}
