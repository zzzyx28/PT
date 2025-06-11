package com.example.test1.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.test1.mapper.TorrentMapper;
import com.example.test1.entity.Torrent;
import com.example.test1.exception.BencodeException;
import com.example.test1.exception.TorrentProcessingException;
import com.example.test1.service.TorrentService;
import com.example.test1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/torrents")
public class TorrentController {

    @Autowired
    private TorrentService torrentService;
    @Autowired
    private TorrentMapper torrentMapper;
    @Autowired
    private UserService userService;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadTorrent(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", required = false) Integer category,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "uid", required = false) String uid,
            Principal principal) {
        try {
            userService.addExperience(uid,100);
            userService.addMagicValue(uid,100);
            Torrent torrent = torrentService.uploadAndSaveTorrent(file, category, description, principal,uid);
            return ResponseEntity.ok(torrent);
        } catch (TorrentProcessingException e) {
            // 处理服务层抛出的业务异常
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的异常
            return ResponseEntity.internalServerError().body("服务器错误: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listTorrents(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<Torrent> torrents = torrentService.listTorrents(page, size);
            return ResponseEntity.ok(torrents);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("获取种子列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> listByOwner(
            @PathVariable String ownerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Torrent> result = torrentService.listByOwnerId(ownerId, page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("查询失败: " + e.getMessage());
        }
    }

    /**
     * 带条件查询接口示例
     */
    @GetMapping("/owner/condition")
    public ResponseEntity<?> listByOwnerWithCondition(
            @RequestParam String ownerId,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "create_time DESC") String orderBy) {
        try {
            List<Torrent> result = torrentService.listByOwnerWithCondition(
                    ownerId, category, orderBy);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("查询失败: " + e.getMessage());
        }
    }

    private Torrent getTorrentById(String torrentId) {
        Torrent torrent = torrentMapper.selectById(torrentId);
        if (torrent == null) {
            throw new TorrentProcessingException("种子不存在");
        }
        return torrent;
    }
    @GetMapping("/download/{torrentId}")
    public ResponseEntity<Resource> downloadTorrent(
            @PathVariable String torrentId) {
        try {
            // 1. 获取种子文件资源
            Resource resource = torrentService.downloadTorrent(torrentId);

            // 2. 获取种子信息用于文件名
            Torrent torrent = getTorrentById(torrentId);

            // 3. 构建响应
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            torrentService.getTorrentContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + torrent.getFilename() + "\"")
                    .body(resource);
        } catch (TorrentProcessingException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // 辅助方法（如果TorrentService中没有）

}