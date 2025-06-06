package com.example.test1.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.test1.entity.Forum;
import com.example.test1.exception.ForumOperationException;
import com.example.test1.mapper.ForumMapper;
import com.example.test1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumService {

    @Autowired
    private ForumMapper forumMapper;

    @Transactional(rollbackFor = Exception.class)
    public Forum createForum(Forum forum, String ownerId) {
        forum.setForumId(java.util.UUID.randomUUID().toString());
        forum.setStatus(Forum.Status.DRAFT);
        forum.setOwnerId(ownerId);
        forum.setCreateTime(LocalDateTime.now());
        forum.setUpdateTime(LocalDateTime.now());

        int rows = forumMapper.insert(forum);
        if (rows != 1) {
            throw new ForumOperationException("创建帖子失败");
        }

        return forum;
    }

    public List<Forum> listForums(int page, int size) {
        Page<Forum> pageRequest = new Page<>(page, size);
        Page<Forum> resultPage = forumMapper.selectPage(pageRequest, new QueryWrapper<>());
        return resultPage.getRecords();
    }

    public List<Forum> listForumsByCategory(int category, int page, int size) {
        Page<Forum> pageRequest = new Page<>(page, size);
        Page<Forum> resultPage = forumMapper.selectPage(pageRequest, new QueryWrapper<Forum>().eq("category", category));
        return resultPage.getRecords();
    }

    public void deleteForum(String forumId) {
        int rows = forumMapper.deleteById(forumId);
        if (rows != 1) {
            throw new ForumOperationException("删除帖子失败");
        }
    }
}
