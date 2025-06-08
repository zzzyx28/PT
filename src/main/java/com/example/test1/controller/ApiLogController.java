package com.example.test1.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.test1.entity.ApiLog;
import com.example.test1.mapper.ApiLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class ApiLogController {

    @Autowired
    private ApiLogMapper apiLogMapper;

    /**
     * 获取全部日志数据
     */
    @GetMapping("/list")
    public List<ApiLog> getAllLogs() {
        return apiLogMapper.selectList(new QueryWrapper<>());
    }

    /**
     * 获取最近 N 条日志
     */
    @GetMapping("/latest")
    public List<ApiLog> getLatestLogs(@RequestParam(defaultValue = "50") int limit) {
        QueryWrapper<ApiLog> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("timestamp").last("LIMIT " + limit);
        return apiLogMapper.selectList(wrapper);
    }

    /**
     * 根据方法统计请求数量（示例：GET、POST等）
     */
    @GetMapping("/count-by-method")
    public List<Map<String, Object>> countByMethod() {
        return apiLogMapper.countGroupByMethod();
    }
}
