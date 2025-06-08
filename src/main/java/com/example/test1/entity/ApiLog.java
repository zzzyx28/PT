package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_log")
public class ApiLog implements Serializable {
    public ApiLog(String method, String path,String requestBody, int status, String responseBody, long duration) {
        this.duration = duration;
        this.method = method;
        this.path = path;
        this.status = status;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
    }

    private String method;
    private String path;
    private String requestBody;
    private int status;

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String responseBody;
    private long duration;

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public int getStatus() {
        return status;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public long getDuration() {
        return duration;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
}
