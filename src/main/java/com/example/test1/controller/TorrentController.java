package com.example.test1.controller;

import com.example.test1.entity.Torrent;
import com.example.test1.exception.BencodeException;
import com.example.test1.exception.TorrentProcessingException;
import com.example.test1.service.TorrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/upload")
    public ResponseEntity<?> uploadTorrent(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", required = false) Integer category,
            @RequestParam(value = "description", required = false) String description,
            Principal principal) {
        try {
            Torrent torrent = torrentService.uploadAndSaveTorrent(file, category, description, principal);
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
}