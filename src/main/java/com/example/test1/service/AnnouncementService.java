package com.example.test1.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.test1.entity.Announcement;
import com.example.test1.exception.AnnouncementException;
import com.example.test1.mapper.AnnouncementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Transactional(rollbackFor = Exception.class)
    public Announcement createAnnouncement(Announcement announcement, String publisherId) {
        announcement.setId(java.util.UUID.randomUUID().toString());
        announcement.setPublisherId(publisherId);
        announcement.setStatus(Announcement.Status.DRAFT);
        announcement.setCreateTime(LocalDateTime.now());
        announcement.setUpdateTime(LocalDateTime.now());

        int rows = announcementMapper.insertAnnouncement(announcement);
        if (rows != 1) {
            throw new AnnouncementException("公告创建失败");
        }

        return announcement;
    }

    public Announcement getAnnouncementById(String id) {
        return announcementMapper.selectById(id);
    }

    public Page<Announcement> listAnnouncements(int page, int size) {
        int offset = (page - 1) * size;
        List<Announcement> records = announcementMapper.selectPageList(offset, size);

        long total = countAll(); // 可以优化为缓存或单独 SQL 查询总数
        Page<Announcement> result = new Page<>(page, size);
        result.setRecords(records);
        result.setTotal(total);
        return result;
    }

    public long countAll() {
        return announcementMapper.selectCount(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Announcement updateAnnouncement(Announcement announcement) {
        announcement.setUpdateTime(LocalDateTime.now());
        int rows = announcementMapper.updateAnnouncement(announcement);
        if (rows != 1) {
            throw new AnnouncementException("公告更新失败");
        }
        return announcement;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAnnouncement(String id) {
        int rows = announcementMapper.deleteById(id);
        if (rows != 1) {
            throw new AnnouncementException("公告删除失败");
        }
    }

    public Page<Announcement> searchByTitle(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        List<Announcement> records = announcementMapper.selectByTitleLike(keyword, offset, size);
        long total = countByTitleLike(keyword); // 可选：统计总数用于分页

        Page<Announcement> resultPage = new Page<>(page, size);
        resultPage.setRecords(records);
        resultPage.setTotal(total);

        return resultPage;
    }

    // 可选：添加 count 方法
    public long countByTitleLike(String keyword) {
        return announcementMapper.countByTitleLike(keyword);
    }

}
