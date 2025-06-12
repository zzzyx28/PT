package com.example.test1.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.example.test1.entity.ApiLog;
import com.example.test1.mapper.ApiLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class RequestLogAspect {

    @Autowired
    private ApiLogMapper apiLogMapper;

    @Pointcut("execution(* com.example.test1.controller..*.*(..))")
    public void requestLog() {}

    @Around("requestLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        // 构建参数 Map，并处理 MultipartFile
        Map<String, Object> params = new HashMap<>();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                if (args[i] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[i];
                    // 只记录文件名和大小，避免直接序列化 MultipartFile
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("fileName", file.getOriginalFilename());
                    fileInfo.put("fileSize", file.getSize());
                    params.put(paramNames[i], fileInfo);
                } else {
                    params.put(paramNames[i], args[i]);
                }
            }
        }

        String url = request != null ? request.getRequestURL().toString() : "N/A";
        String httpMethod = request != null ? request.getMethod() : "N/A";

        Object result = null;
        int status = 200;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            status = 500;
            throw ex;
        } finally {
            stopWatch.stop();
            long duration = stopWatch.getTotalTimeMillis();

            // 处理返回值：特殊处理 Resource 类型（文件下载）
            String resultJson;
            if (result instanceof ResponseEntity) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                if (responseEntity.getBody() instanceof Resource) {
                    resultJson = "{type: \"Resource\", downloadable: true}";
                } else {
                    resultJson = JSON.toJSONString(responseEntity.getBody());
                }
            } else {
                // 使用 PropertyFilter 过滤不可序列化的对象
                PropertyFilter filter = (object, name, value) ->
                        !(value instanceof MultipartFile || value instanceof Resource);
                resultJson = JSON.toJSONString(result, filter);
            }

            // 序列化参数
            PropertyFilter paramFilter = (object, name, value) ->
                    !(value instanceof MultipartFile || value instanceof Resource);
            String paramsJson = JSON.toJSONString(params, paramFilter);

            ApiLog apiLog = new ApiLog(
                    httpMethod,
                    url,
                    paramsJson,
                    status,
                    resultJson,
                    duration
            );
            apiLogMapper.insert(apiLog);
        }
    }
}