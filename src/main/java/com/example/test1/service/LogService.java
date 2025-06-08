//package com.example.test1.service;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//
//
//import com.example.test1.entity.ApiLog;
//import com.example.test1.mapper.ApiLogMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Service
//public class LogService {
//
//    private final List<ApiLog> logs = Collections.synchronizedList(new ArrayList<>());
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    private final ApiLogMapper apiLogMapper; // 注入 Mapper
//
//    @Autowired
//    public LogService(SimpMessagingTemplate messagingTemplate, ApiLogMapper apiLogMapper) {
//        this.messagingTemplate = messagingTemplate;
//        this.apiLogMapper = apiLogMapper;
//    }
//
//
//    public void logRequest(ApiLog log) {
//        // 过滤不需要记录的路径
//        if (log != null && log.getPath() != null &&
//                (log.getPath().startsWith("/douyin/monitor/metrics") ||
//                        log.getPath().startsWith("/douyin/monitor/logs"))) {
//            return; // 直接返回，不记录这些路径的请求
//        }
//
//        // 保存到数据库
//        apiLogMapper.insert(log);
//
//        // 添加到内存列表（2000）
//        logs.add(log);
//        if (logs.size() > 2000) {
//            logs.remove(0);
//        }
//
//        // 通过WebSocket发送实时日志
//        messagingTemplate.convertAndSend("/topic/logs", log);
//    }
//
//    public List<ApiLog> getAllRecentLogs(int limit) {
//        if (limit <= 0) {
//            limit = 500; // 默认值
//        }
//        // 按创建时间倒序排序，取前 limit 条
//        LambdaQueryWrapper<ApiLog> wrapper = new LambdaQueryWrapper<>();
//        wrapper.orderByDesc(ApiLog::getTimestamp) // 按创建时间倒序
//                .last("LIMIT " + limit); // 限制结果数量
//
//        return apiLogMapper.selectList(wrapper);
//    }
//
//
//    public List<ApiLog> getRecentLogs(int count) {
//        int start = Math.max(0, logs.size() - count);
//        return new ArrayList<>(logs.subList(start, logs.size()));
//    }
//
//}