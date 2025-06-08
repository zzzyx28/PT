package com.example.test1.aspect;

import com.alibaba.fastjson.JSON;
import com.example.test1.entity.ApiLog;
import com.example.test1.mapper.ApiLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
        HttpServletRequest request = attributes != null ?  attributes.getRequest() : null;

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> params = new HashMap<>();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                params.put(paramNames[i], args[i]);
            }
        }

        String url = request != null ? request.getRequestURL().toString() : "N/A";
        String httpMethod = request != null ? request.getMethod() : "N/A";

//        log.info("➡️ 请求开始 [{} {}] {}.{} | 参数: {}",
//                httpMethod, url, className, methodName,
//                JSON.toJSONString(params));

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

            ApiLog apiLog = new ApiLog(
                    httpMethod,
                    url,
                    JSON.toJSONString(params),
                    status,
                    JSON.toJSONString(result),
                    duration
            );
            apiLogMapper.insert(apiLog);

//            log.info("⬅️ 请求结束 [{} {}] {}.{} | 耗时: {}ms | 返回: {}",
//                    httpMethod, url, className, methodName,
//                    duration,
//                    JSON.toJSONString(result));
        }
    }
}
