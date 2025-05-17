package com.example.test1.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.test1.entity.Announcement;
import com.example.test1.exception.AnnouncementException;
import com.example.test1.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @PostMapping("/create")
    public ResponseEntity<?> createAnnouncement(@RequestBody Announcement announcement, Principal principal) {
        try {
            String publisherId = Optional.ofNullable(principal)
                    .map(p -> p.getName())
                    .orElse("admin");

            Announcement created = announcementService.createAnnouncement(announcement, publisherId);
            return ResponseEntity.ok(created);
        } catch (AnnouncementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("服务器错误: " + e.getMessage());
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<?> getAnnouncement(
            @RequestParam(value = "announcementId") String id) {
        try {
            return ResponseEntity.ok(announcementService.getAnnouncementById(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("获取公告失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listAnnouncements(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(announcementService.listAnnouncements(page, size));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("获取公告列表失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAnnouncement(@RequestBody Announcement announcement) {
        try {
            return ResponseEntity.ok(announcementService.updateAnnouncement(announcement));
        } catch (AnnouncementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("更新公告失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAnnouncement(
            @RequestParam(value = "announcementId") String id) {
        try {
            announcementService.deleteAnnouncement(id);
            return ResponseEntity.ok("删除成功");
        } catch (AnnouncementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("删除失败: " + e.getMessage());
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchAnnouncementsByTitle(
            @RequestParam String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            Page<Announcement> result = announcementService.searchByTitle(keyword, page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("搜索失败: " + e.getMessage());
        }
    }

}
