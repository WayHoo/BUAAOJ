package cn.edu.buaa.onlinejudge.utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.servlet.http.HttpServletResponse;

@ApiModel("HTTP响应消息实体")
public class HttpResponseWrapperUtil {
    @ApiModelProperty(value = "响应消息的数据")
    private Object data;
    @ApiModelProperty(value = "响应消息的状态码")
    private int status;
    @ApiModelProperty(value = "响应消息的提示信息")
    private String info;

    public HttpResponseWrapperUtil(Object data, int status, String info) {
        this.data = data;
        this.status = status;
        this.info = info;
    }

    public HttpResponseWrapperUtil(Object data){
        this(data, HttpServletResponse.SC_OK, "success");
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "HttpResponseWrapperUtil{" +
                "data=" + data +
                ", status=" + status +
                ", info='" + info + '\'' +
                '}';
    }
}