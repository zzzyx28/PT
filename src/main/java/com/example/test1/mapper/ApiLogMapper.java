package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.test1.entity.ApiLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiLogMapper extends BaseMapper<ApiLog> {
    @Select("SELECT method, COUNT(*) as count FROM api_log GROUP BY method")
    List<Map<String, Object>> countGroupByMethod();
}