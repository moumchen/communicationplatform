package com.jixiata.model.Vo;

import com.jixiata.util.ConstantEnum;

public class ResponseVo<T> {
    private Boolean success;
    private T data;
    private String message;
    private Integer statusCode;
    private Integer totalCount;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T date) {
        this.data = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResponseVo getResponseVo(boolean success, Object data, Integer statusCode, String message){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setMessage(message);
        responseVo.setData(data);
        responseVo.setStatusCode(statusCode);
        responseVo.setSuccess(success);
        return responseVo;
    }
    public static ResponseVo getResponseVo(boolean success, Object data, Integer statusCode, String message,Integer totalCount){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setMessage(message);
        responseVo.setData(data);
        responseVo.setStatusCode(statusCode);
        responseVo.setSuccess(success);
        responseVo.setTotalCount(totalCount);
        return responseVo;
    }
    public static ResponseVo getSuccessResponseVo(){
        return ResponseVo.getResponseVo(true, true, ConstantEnum.SUCCESS.getStatusCode(), "操作成功");
    }

    public static ResponseVo getPermissonDeniedResponseVo(){
        return ResponseVo.getResponseVo(false, null, ConstantEnum.PERMISSION_DENIED.getStatusCode(), "未登录");
    }

    public static ResponseVo getFailResponseVo(){
        return ResponseVo.getResponseVo(false, null, ConstantEnum.FAIL.getStatusCode(), "操作失败");
    }

    public static ResponseVo getParamErrorResponseVo(){
        return ResponseVo.getResponseVo(false,null,ConstantEnum.CHECK_FAIL.getStatusCode(),"参数错误");
    }
    public static ResponseVo getParamErrorAndInfoResponseVo(String info){
        return ResponseVo.getResponseVo(false,null,ConstantEnum.CHECK_FAIL.getStatusCode(),"参数错误:"+info);
    }

    public static ResponseVo getIdentityDeniedResponseVo(){
        return ResponseVo.getResponseVo(false,null,ConstantEnum.IDENTITY_DENIED.getStatusCode(),"权限禁止");
    }

    public static ResponseVo getFailResponseVoByMessage(String msg){
        return ResponseVo.getResponseVo(false,null,ConstantEnum.FAIL.getStatusCode(), msg);
    }
}
